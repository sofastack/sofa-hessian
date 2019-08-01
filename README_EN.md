# SOFA-Hessian

[![Build Status](https://travis-ci.com/sofastack/sofa-hessian.svg?branch=master)](https://travis-ci.com/sofastack/sofa-hessian)
[![Coverage Status](https://codecov.io/gh/sofastack/sofa-hessian/branch/master/graph/badge.svg)](https://codecov.io/gh/sofastack/sofa-hessian)
![License](https://img.shields.io/badge/license-Apache--2.0-green.svg)

[Hessian](http://hessian.caucho.com/#Java) is a high-performance and high-compatibility binary serialization protocol.

SOFA-Hessian is an internal improvement version based on native Hessian v4.0.51. At present, It has been used internally for many years in Ant Financial. We have fixed some bugs, enhanced some functions, and add some features, includes: 

- Add generic serialization.
- Add `ClassNameResolver` & `ClassNameFilter` for the mapping, converting or filtering of the class name.
- Add serialization blacklist from Ant Security Team.
- Improves map locking of the cache in `SerializerFactory`.
- See more at [Improvements](https://github.com/sofastack/sofa-hessian/wiki/Improvements).


## Required
 - JDK 6 or above.

## Documents
 - [Release Notes](https://github.com/sofastack/sofa-hessian/wiki/ReleaseNotes)
 - [User Guide](https://github.com/sofastack/sofa-hessian/wiki/UserGuide)
 - [Improvements](https://github.com/sofastack/sofa-hessian/wiki/Improvements)
 - [Road Map](https://github.com/sofastack/sofa-hessian/wiki/RoadMap)

## License
 - [Apache License 2.0](https://github.com/sofastack/sofa-hessian/blob/master/LICENSE)