package models;

import assets.enums.UserType;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by PENTAGON on 30.07.14.
 */
@Entity(name = "SysUser")
public class SysUser extends Model {
    @Id
    public Long id;

    public UserType userType;
    public String name;
    public String contactPerson;
    public String email;
    public String password;
    public String phoneNumber;
}
