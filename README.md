# Tarnish

Tarnish RSPS updated public release

### Setup

* First, clone this repository.
  `git clone https://github.com/Jire/tarnish.git`  
  (or use IntelliJ IDEA's built-in VCS)
* Next, open the project in IntelliJ IDEA.
* Make sure you have JDK `21` and JDK `11` (client requires it) both installed in IntelliJ settings. Make sure to select
  JDK `21` as the
  default for the project.
* Download the latest cache:
  [https://files.jire.org/cache-tarnish-218.zip](https://files.jire.org/cache-tarnish-218.zip)
* Place the latest cache into the [game-server/data/cache](game-server/data/cache/) directory.
* Run the `Run Server` IDEA run configuration to start the server.  
  ![Running the server](https://i.imgur.com/fOvP7QZ.png)
* Now, setup the [SwiftFUP cache file server](https://github.com/Jire/SwiftFUP)
  with the latest cache in its
  [server/cache](https://github.com/Jire/SwiftFUP/tree/main/server/cache) directory.
* Now, run the `Run Client` IDEA run configuration to start the client.  
  ![Running the client](https://i.imgur.com/S7sVjus.png)
* Enjoy playing!