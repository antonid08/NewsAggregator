This document describes high-level application architecture and more detailed feature design.

# High-level app architecture

## Common code organization convention
This application is pretty small, so we can afford even don't have any convention. But let's consider situation when other developers will modify/extend it and select one.
If we want other developers (and us) maintain and extend the system, it's better to follow one of common architecture patterns. 

We consider these ones: 
* MVI
* Google recommended Android architecture (MVVM + Repository)
* Clean Architecture.

I prefer to choose MVI if the system has complex state. Current system state doesn't look complex, so let's move to more common patterns.  
This application looks pretty simple, but this is not so. We'll need to combine data sources, implement pagination on them, handle errors, operate data and so on. Most likely, just repository will not be enough. We need to encapsulate all these actions and Clean Architecture has well-defined abstractions for it. Also, it's more common.

Code will be organized using **Clean Architecture**.

## Extensibility
If the application is going to be huge (or just big), best option for extensibility is put features to _feature modules_. This approach requires additional effort on startup: planning common modules and dependencies sharing/injection to feature modules.  
Another option is to use modules related to_ clean architecture layers_, but it requires effort like feature modules and doesn't so flexible, so reject it.

In current case we don't plan to extend, so we can just put all code in single _app_ module. That's what we will use. It's easy to convert to feature modules, but when it will be required, don't forget to design things i've described above.
_app_ module is planned to be small, so it's suitable to organize packages by Clean Architecture layers (if additional functionality required, it will be implemented in other feature module).
#### High-level package structure for features:
* ui
* domain
* data
    * remote
    * local

## Error handling and fault tolerance
TBD

# News feature design
## Data
TBD
## Paging
TBD
## Errors handling
TBD
## Background check for updates
TBD
## Main classes 
