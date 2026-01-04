# 2.4.3

# TASK LIST:
* Disable the Loggers in LoggerConfig 
* Fix the Permission Code in LoggerScreenCommand
* NeoForge Rendering bug, GLOBAL SINGLEPLAYER AS WELL

## Changed

* Massive cleanup in the way we register Codecs and Data ComponentTypes.

## Bug Fixes

* Fixed a critical issue where Fabric Server where enable to be started due to trying to load Client sided Code on the
  Server.
* Fixed a critical issue where Clients would crash when trying to send a Packet.
* Fixed a crash where the Client was looking for the Controller file.