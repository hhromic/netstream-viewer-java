netstream-viewer-java
=====================

A NetStream (GraphStream) Live Graph Viewer written in Java.

This small but handy program creates a live graph display for incoming NetStream [1] events. The underlying graph library is GraphStream [2] using a MultiGraph [3] implementation. For example, this viewer can be used with NetLogo [4] using the GraphStream NetLogo extension [5].

You can customize the included ```stylesheet.css``` file for your taste. Check the CSS reference for GraphStream [6]. You can adjust settings in the ```viewer.properties``` file such as the network port to use, drawing quality and some graph layout parameters.

References
----------

1. https://github.com/graphstream/gs-netstream/wiki/NetStream-Manual
2. http://graphstream-project.org/
3. http://graphstream-project.org/api/gs-core/org/graphstream/graph/implementations/MultiGraph.html
4. https://ccl.northwestern.edu/netlogo/
5. https://github.com/graphstream/gs-netlogo
6. http://graphstream-project.org/doc/Tutorials/GraphStream-CSS-Reference_1.0/
