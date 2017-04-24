# Ylinor - The Ylinor voxel MMORPG

## Setting up development environment

**You need Java 8**

First import the project. Then :

* Create a "Client" run configuration, with as run directory : "client/core/assets".
* Create a "Server" run configuration, with as run directory : "server/run/". **If you want to test the client with your local server, you need to add --debug to the program arguments of the Client.**

## Contributing

---------

Do not do this :

```java
if (condition)
	do that;
```

Do this :

```java
if (condition)
{
	do that;
}
````
