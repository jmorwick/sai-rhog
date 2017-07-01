# SAI-RHOG
Wrappers for the [RHOG graph library](https://github.com/santiontanon/RHOG) for use with the [SAI framework](https://github.com/jmorwick/sai) for experimenting with structured data. 

Hooks for SAI in to RhoG included:
* Factories for generating RhoG directed labeled graph objects from SAI graphs (DLGFactory and TreeDLGFactory)
* An interface for using an existing DLG object as a SAI graph (SaiDlgAdapter)
* Wrappers for serialization/deserialization of GML (reference static methods in GMLUtil)
