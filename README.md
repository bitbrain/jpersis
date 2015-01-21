![jpersis](logo.png)
=======

Light weighted persistence framework for Java.


[![Build Status](https://travis-ci.org/MyRealityCoding/jpersis.svg?branch=master)](https://travis-ci.org/MyRealityCoding/jpersis)

To use the latest snapshot ```jpersis-1.0-SNAPSHOT``` add the following dependency to your project:
```maven
<dependency>
   <artifactId>jpersis</artifactId>
   <groupId>com.github.myrealitycoding</groupId>
   <version>1.0-SNAPSHOT</version>
</dependency>
```

Getting started
===

This framework provides easy-to-write data mapping. Start [with the wiki here](https://github.com/MyRealityCoding/jpersis/wiki).


Case study: An own customer mapper
===

A sample customer mapper could look like this:

```java
@Mapper("de.myreality.test.models.Customer")
public interface CustomerMapper {
    
    @Select
    Collection<Customer> findAll();

    @Select(condition = "customer_id = $1")
    Customer findById(int id);
    
    @Select(condition = "first_name = $1")
    Customer findByFirstName(String firstName);
    
    @Insert
    void insert(Customer customer);
    
    @Insert
    void insert(Collection<Customer> customer);
    
    @Count
    int CountAll();
    
    @Update
    void update(Customer customer);
    
    
    @Update
    void update(Collection<Customer> customer);
    
    @Delete
    void delete(Customer customer);
       
    @Delete
    void delete(Collection<Customer> customer);
    
}
```

You only need to provide the model ```Customer``` for that.

### How to design a proper model

Jpersis needs to know how to build the model. Therefore we need to define it in Java first:

```java
public class Customer {

	@PrimaryKey(true)
	private int id;

	private String name;

	private String adress;

	/* A default constructor is needed */
	public Customer() { 

	}

	public String getName() {
		return name;
	}
}
```
Jpersis will create a table for you and maps the given values properly.

Credits
===
Documentation, API and development by [Miguel Gonzalez](http://my-reality.de). Feel free to send pull requests. <3
