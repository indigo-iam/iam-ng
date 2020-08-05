# IAM Dashboard

[![codecov](https://codecov.io/gh/stfc/iam-dashboard/branch/master/graph/badge.svg)](https://codecov.io/gh/stfc/iam-dashboard)
![](https://github.com/stfc/iam-dashboard/workflows/Build,%20test,%20publish%20(master)/badge.svg)

A dashboard for the IAM NG project, interfacing with the API and Keycloak (https://github.com/indigo-iam/iam-ng).

## Getting started

To get started with the project - make sure you have installed NodeJS and npm.

Follow the docker-compose instructions [here](https://github.com/indigo-iam/iam-ng/blob/master/compose/README.md). Make sure to have added the appropriate entries to your /etc/hosts or platform equivalent.

Clone the repository into a directory of your choosing.

Run `npm ci` in the root of the cloned repository. This will install all the
dependencies required for the project, into a node_modules folder. You may also
wish to install the Angular CLI globally using `npm install -g @angular/cli`.

Edit appropriate configuration values in src/assets/app-config.json (does not
need a recompile to take effect).

Run `ng serve` to run a local copy of the website on port 4000 with the Angular
development server.

## Build

Production builds are automatically generated on every commit to master as GitHub artifacts. 
A docker image with a reverse proxy is also avaliable at: https://hub.docker.com/r/magicalwill/iam-dashboard
If you want to do it manually:
Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build. 

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io). This will open a dedicated Chrome window to run tests.

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

n.b. there are currently no e2e tests configured

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
