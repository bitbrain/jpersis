# Getting Started

## Table of contents

[I. Why JPersis?](#1-why-jpersis)<br />
[II. Models](#2-models)<br />
[III. Mappers](#3-mappers)<br />
[IV. Example](#4-example)<br />
[V. Drivers](#5-drivers)<br />
[VI. Annotations](#6-annotations)<br />
[VII. Naming](#7-name-converters)<br />
[VIII. References](#8-references)<br />

<a href="#" name="1-why-jpersis"></a>
# I. Why JPersis?

When using an object oriented language like Java it is time consuming to map data manually from a database to models and vise versa. You have to remember data types, validate input and output and you have to change your code for each use case. JPersis is a light-weighted library which does not require much configuration. You define a datastore, bind a model to it and use so called mappers to interact with the datastore.

<a href="#" name="2-models"></a>
# II. Models
A model is a simple Java class with attributes. There are some restrictions to fullfill the role:

* each model has to annotate one and only one field by a ```@PrimaryKey``` annotation
* except ```Date``` no other classes are allowed as field data types
* if a model has a custom field data type, it has to be annotated by ```@Ignored```
* a model needs a public constructor without arguments

Imagine we have a model, called ```User```, it could look something like this:

```java
public class User {

    @PrimaryKey 
    private int id;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```
You have created a valid JPersis model, because all requirements are fullfilled.

<a href="#" name="3-mappers"></a>
# III. Mappers
Now we need to do something with our model. We want to store, read and manipulate data. Each model needs a mapper which is responsible. To implement a mapper you have the following restrictions:

* each mapper has to be annotated by a ```@Mapper``` annotation which also provides a correct class path to the model
* each mapper has to be an interface
* each method of the mapper needs to be annotated by a supported annotation (See [VI. Annotations](#6-annotations)).

A sample mapper for our previously created model could look something like this:

```java
@Mapper("my.package.to.the.model.User")
public interface UserMapper {

   @Insert
   boolean insert(User user);

   @Delete
   boolean delete(User user);

   @Count
   int count();

   @Select(condition = "userName = $1")
   User findByUserName(String userName);
}
```
That's it! You have created your first mapper.
<a href="#" name="4-example"></a>
# IV. Example
Now we want some action. We want to use a SQLite datasource. We create a driver first (provided by JPersis) and create an ```JPersis``` object:
```java
Driver driver = new SQLiteDriver("database.sql");
JPersis jpersis = new JPersis(driver);
```
Now our application is ready to use. Let's create a user first:
```java
User user = new User();
user.setName("Max");
```
Next we need our mapper. JPersis will provide a mapper for you:
```java
UserMapper mapper = jpersis.map(UserMapper.class);
```
Let's insert our user and check if it worked:
```java
mapper.insert(user); // returns true, seems to be okay!
mapper.count(); // returns 1, there is a user in the database
mapper.findByName("Max"); // Hey Max, how are you doing?
```
Nothing more to do. JPersis created a user table internally and inserted the user into it. If we want to avoid custom mappers, we can fall back to default mapping:

```java
DefaultMapper<User> mapper = jpersis.mapDefault(User.class);
Collection<User> allUsers = mapper.find();

// We do not want to query always the datastore.
// Instead we are using caching
// Equivalent to:
// CachedDefaultMapper<User> cachedMapper = jpersis.mapDefaultCached(User.class);
CachedDefaultMapper<User> cachedMapper = jpersis.cached(mapper);
```

<a href="#" name="5-drivers"></a>
# V. Drivers
To interact with the database, a so called **Driver** is required. A driver maps a general functionality to a technology based functionality (like a general insert command to an MongoDB insert). Furthermore a driver is used by annotation methods.

By default, the following drivers will be supported:
* SQLiteDriver
* MySQLDriver
* PostgreSQLDriver
* CassandraDriver (not implemented yet)
* Neo4jDriver (not implemented yet)
* MongoDriver (not implemented yet)

<a href="#" name="6-annotations"></a>
# VI. Annotations

The annotations provided by Jpersis are predefined but it is possible to register custom annotations to Jpersis. Given the following annotation:
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReturnOne { }
```
Annotating a method with this annotation should always return for example the value `1`. To do so we need to define a so called `MapperMethod`:
```java
public class ReturnOneMethod extends AbstractMapperMethod<ReturnOne> {

  public ReturnOneMethod(ReturnOne one) {
    super(one);
  }

  @Override
  public void on(Class<?> model, Object[] params, Query query) {
    // we build our query internally to count one element
    // note that this would return 0 if no data is available
    query.limit(1).count();
  }

  @Override
  protected Class<?>[] supportedReturnTypes(Class<?> model) {
    return new Class<?>[] { Integer.class, int.class };
  }
}
```
Finally we need to register our mapper method:
```java
jpersis.register(ReturnOne.class, ReturnOneMethod.class);
```
We are now able to annotate mappers with the `@ReturnOne` annotation:
```java
public interface MyMapper {
    /* ... */
    @ReturnOne
    int returnOneOrZero();
    /* ... */
}
```
<a href="#" name="7-name-converters"></a>
# VII. Naming
It is also possible to customize conversion between Java and database names. To do so, you have to define a ```Naming```. By default, the ```CamelCaseNaming``` is used:
```
myField -> my_field
my_field -> myField
MyClass -> my_collection
my_collection -> MyCollection
```
To do so, implement your own converter:
```java
public class CustomNaming implements Naming {
   
   @Override
   public String collectionToJava(String name) {
      return name;
   }

   @Override
   public String javaToCollection(String name) {
      return name;
   }

   @Override
   public String fieldToJava(String name) {
      return name;
   }

   @Override
   public String javaToField(String name) {
      return name;
   }
}
```
Afterwards you can set your converter:
```java
jpersis.setNaming(new CustomNaming());
```
# References
<a href="#" name="8-references"></a>
Later on, jpersis will support model references:
```java
public class CarPark {

   @PrimaryKey
   private String id;

   @Reference("de.test.Car")
   private Collection<Car> cars;
}
```
This will give you all cars which are referenced to this object.
