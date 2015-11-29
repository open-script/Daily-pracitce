<?php

/**
 * Created by PhpStorm.
 * User: zzt
 * Date: 11/29/15
 * Time: 8:32 PM
 */
class SignUser extends DomainObject
{
    private $name;
    private $email;
    private $password;

    /**
     * SignUser constructor.
     * @param $name
     * @param $email
     * @param $password
     */
    public function __construct($uid, $name, $email, $password)
    {
        parent::__construct($uid);
        $this->name = $name;
        $this->email = $email;
        $this->password = $password;
    }

    /**
     * @return mixed
     */
    public function getName()
    {
        return $this->name;
    }

    /**
     * @return mixed
     */
    public function getEmail()
    {
        return $this->email;
    }

    /**
     * @return mixed
     */
    public function getPassword()
    {
        return $this->password;
    }

    function getData()
    {
        // the order of this is fixed, see UserMap insertStmt
        return array($this->name, $this->email, $this->password);
    }
}