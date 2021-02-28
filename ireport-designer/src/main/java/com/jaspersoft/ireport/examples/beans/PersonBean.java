/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of iReport.
 *
 * iReport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * iReport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with iReport. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.ireport.examples.beans;

/**
 *
 * @author  Administrator
 */
public class PersonBean {
    
    private String firstName;
    
    private String lastName;
    
    private String email;

    private HobbyBean[] hobbies;
    
    private AddressBean address;
    
    /** Creates a new instance of PersonBean */
    public PersonBean() {
	this(null);
    }
    
    public PersonBean(String name) {
    	this.setFirstName( name );
        hobbies = new HobbyBean[0];
    }
    
    /**
     * Getter for property firstName.
     * @return Value of property firstName.
     */
    public java.lang.String getFirstName() {
        return firstName;
    }
    
    /**
     * Setter for property firstName.
     * @param firstName New value of property firstName.
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * Getter for property lastName.
     * @return Value of property lastName.
     */
    public java.lang.String getLastName() {
        return lastName;
    }
    
    /**
     * Setter for property lastName.
     * @param lastName New value of property lastName.
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Getter for property hobbies.
     * @return Value of property hobbies.
     */
    public HobbyBean[] getHobbies() {
        return this.hobbies;
    }
    
    /**
     * Setter for property hobbies.
     * @param hobbies New value of property hobbies.
     */
    public void setHobbies(HobbyBean[] hobbies) {
        this.hobbies = hobbies;
    }
    
    /**
     * Getter for property address.
     * @return Value of property address.
     */
    public AddressBean getAddress() {
        return address;
    }
    
    /**
     * Setter for property address.
     * @param address New value of property address.
     */
    public void setAddress(AddressBean address) {
        this.address = address;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
