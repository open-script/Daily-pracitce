from _ctypes import sizeof
from ctypes import LittleEndianStructure, c_char, c_int, c_uint
import ctypes
import os
from learn_class.os.bitmap import BitMap
from learn_class.os.filetype import FileType

__author__ = 'zzt'


class DirEntry(LittleEndianStructure):
    _fields_ = [("filename", ctypes.c_char * 32),
                ("inode_off", ctypes.c_int)]


class Inode(LittleEndianStructure):
    _fields_ = [("size", c_uint),
                ("dev_id", c_int),
                ("index", c_uint * 15),
                ("link_count", c_int),
                ("type", c_int),
                ("pad", c_char * 52)]


class MkImg:
    SECTOR = 512
    INT = 32
    NODE_SZ = sizeof(Inode)

    # argument: ./disk.img, indoe number, block number, block size(in kB)
    def __init__(self, disk, ino, bno, bsize):
        assert self.NODE_SZ == 128
        self.dir = os.path.dirname(disk)
        self.disk = disk
        self.inode_num = ino
        self.block_num = bno
        self.bsize = bsize * 2 ** 10
        self.start = []
        # first size is super block
        self.size = [MkImg.SECTOR, int(ino / BitMap.BITS), int(bno / BitMap.BITS), ino * MkImg.NODE_SZ, bno * bsize]
        self.buf = bytearray(sum(self.size))

    '''
    fill the gap to meet size of a SECTOR
    :param size: size of original buffer
    :return: how many 0 is filled
    '''

    def fill_gap(self, size):
        gap = MkImg.SECTOR - size % MkImg.SECTOR
        if gap == MkImg.SECTOR:
            gap = 0

        for x in range(gap):
            self.buf.append(0)

        return gap

    # def init_map(self):
    #     for x in range(self.size[0] + self.size[1]):
    #         self.buf.append(0)
    #
    # def init_inode_block(self):
    #     for x in range(self.size[2] + self.size[3]):
    #         self.buf.append(0)

    @staticmethod
    def get_area(start, x, unit):
        return start + x * unit

    def make_file(self, type):
        # apply for inode, block
        map1 = BitMap(self.buf, self.start[0], self.size[1])
        x = map1.apply_bit()
        inode_off = self.get_area(self.start[2], x, MkImg.NODE_SZ)
        map2 = BitMap(self.buf, self.start[1], self.size[2])
        y = map2.apply_bit()
        block_off = self.get_area(self.start[3], y, self.bsize)
        # set block offset, type in inode
        node = Inode.from_buffer(self.buf, inode_off)
        # IDE driver
        node.dev_id = 6
        node.index[0] = block_off
        node.link_count = 1
        node.type = type
        return inode_off, block_off

    def init_super(self, start):
        # inode map start after a sector of super start,
        # last start is useless
        for x in self.size:
            self.start.append(start + x)

        def int_to_bytes_bigendian(i):
            j = (2 ** BitMap.BITS - 1) << (MkImg.INT - BitMap.BITS)
            l = []
            for xx in range(MkImg.INT / BitMap.BITS):
                l.append(j & i)
                j >>= BitMap.BITS

            return l

        def int_to_bytes_little_endian(i):
            j = (2 ** BitMap.BITS - 1)
            l = []
            for xx in range(MkImg.INT / BitMap.BITS):
                l.append(j & i)
                j <<= BitMap.BITS

            return l

        for x in range(len(self.size) - 1):
            # TODO little endian?
            for b in int_to_bytes_little_endian(self.start[x]):
                self.buf[start] = b
                start += 1
            for b2 in int_to_bytes_little_endian(self.size[x + 1]):
                self.buf[start] = b2
                start += 1

    def make_img(self):
        disk_sz = os.path.getsize(self.disk)
        # although it is append at end, for they are all zero, so it's same
        super_start = self.fill_gap(disk_sz) + disk_sz
        assert super_start % MkImg.SECTOR == 0
        print(super_start)
        self.init_super(super_start)
        # it is done in __init__
        # self.init_map()
        # self.init_inode_block()
        # init root directory
        # write '.' '..' in block, ie a dir
        inode_o, block_o = self.make_file(FileType.DIR)
        current = DirEntry.from_buffer(self.buf, block_o)
        current.filename = b'.'
        current.inode_off = inode_o
        father = DirEntry.from_buffer(self.buf, block_o + sizeof(current))
        father.filename = b'..'
        father.inode_off = inode_o
        with open(os.path.join(self.dir, 'harddisk.img'), 'wb') as img:
            img.write(bytearray(self.buf))


if __name__ == '__main__':
    print(os.getcwd())
    for x in os.listdir('.'):
        print(x)
    with open('./disk.img', 'rb') as test:
        print()
    a = MkImg('./disk.img', 2 ** 12, 2 ** 20, 1)
    a.make_img()
