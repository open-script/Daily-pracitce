from __future__ import division
from gi.overrides.Gdk import argv
import math
import numpy as np
from hw_scrape.PIE import get_pie
from statistic import pearson_r
from statistic.InferStats import draw
from statistic.InferStats.draw import draw_scatter, draw_curve
from statistic.check.util import average

THRESHOLD = 0.7

__author__ = 'zzt'

PATH = ''
REST_LIST = [0.0000001, 1, 2, 3, 4, 5, 6]

functions = {
    0: lambda x, a, b: [a + b * i for i in x],
    1: lambda x, a, b: [1 / (a + b / i) for i in x],
    2: lambda x, a, b: [math.pow(math.e, a) * math.pow(math.e, b * i) for i in x],
    3: lambda x, a, b: [a + b * math.log(i) for i in x],
    4: lambda x, a, b: [math.pow(math.e, a) * math.pow(i, b) for i in x]
}


def min_2(x, y):
    assert len(x) == len(y)
    x_bar = average(x)
    y_bar = average(y)
    lxy_1 = 0.0
    for i in range(0, len(x)):
        lxy_1 += (x[i] * y[i])
    lxy = lxy_1 - sum(x) * sum(y) / len(x)
    l_xx = sum(a ** 2 for a in x) - sum(x) ** 2 / len(x)
    b = lxy / l_xx
    a = y_bar - b * x_bar
    print('y = {} + {}*b'.format(a, b))
    r = pearson_r.pearson_r(x, y)
    return [r, a, b]


def different_model(x, y):
    r = [0] * 5
    a = [1] * 5
    b = [1] * 5
    # linear
    r[0], a[0], b[0] = min_2(x, y)
    # 1/x
    r[1], a[1], b[1] = min_2([1 / ax for ax in x], [1 / bx for bx in y])
    # e^x
    r[2], a[2], b[2] = min_2(x, [math.log(bx) for bx in y])
    # lnx
    r[3], a[3], b[3] = min_2([math.log(ax) for ax in x], y)
    # x^b
    r[4], a[4], b[4] = min_2([math.log(ax) for ax in x], [math.log(bx) for bx in y])
    r_ = [abs(t) for t in r]
    i = r_.index(max(r_))
    if max(r_) < THRESHOLD:
        return
    else:
        draw_curve(np.linspace(0, 6, 3001), functions[i], a[i], b[i])


def infer_rest_player(player_id, path=PATH):
    rest = get_pie(player_id)[1:]
    assert len(rest) == len(REST_LIST)
    # draw graph
    tmp = []
    final = []
    for i in range(0, len(rest)):
        if rest[i] != 0:
            final.append(rest[i])
            tmp.append(REST_LIST[i])

    draw_scatter(tmp, final, 10)
    if len(tmp) < 3:
        return
    different_model(tmp, final)
    draw.finish(path + player_id)


if __name__ == '__main__':
    if len(argv) == 2:
        infer_rest_player(argv[1])

        # l = [116.5, 120.8, 124.4, 125.5, 131.7, 136.2, 138.7, 140.2]
        # y = [255.7, 263.3, 275.4, 278.4, 296.7, 309.3, 315.8, 318.8]
        # draw_scatter(y, l, 10)
        # different_model(y, l)
        # draw.finish('test')
