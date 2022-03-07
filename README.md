# Test project

[![Android CI](https://github.com/RankoR/WallesterTest/actions/workflows/main.yml/badge.svg)](https://github.com/RankoR/WallesterTest/actions/workflows/main.yml)

## Notes on this project

We can't use 3rd-party libraries. This is a serious limitation for writing «production-ready» code: we need at least a DI for it.

I've tried to write code as production-ready as it's possible:

- Splitting everything to interface/implementation for better testing and (in future) mocking
- Splitting project to modules (see below)
- Testing almost all business logic: see `tests` and `androidTests` in modules
- Basic CI set up (CI runs JUnit and Instrumented tests, builds and publishes APK)

However, there are lots of things we can improve (but it wasn't done due to time limitation):

- Implement some basic DI
- Implement basic navigation
- Split to more modules with better naming
- Do a better UX/UI
- Add an «error» state to the list screen
- Write ViewModel and UI tests