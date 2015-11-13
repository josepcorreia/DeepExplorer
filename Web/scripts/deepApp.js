angular.module('deepApp', [
  'deepApp.services',
  'deepApp.controllers',
  'ngRoute'
]).

config(function($routeProvider, $locationProvider) {

    $routeProvider.
            // route for the home page
            when('/', {
                templateUrl : 'partials/home.html',
                controller  : 'searchCtrl'
            }).

            // route for the about page
            when('/wordexplorer', {
                templateUrl : 'partials/deep_result.html',
                controller  : 'deepCtrl'
            }).
            when('/about', {
                templateUrl : 'partials/about.html',
                controller  : 'deepCtrl'
            }).

            otherwise({
              redirectTo: '/'
          });

        // use the HTML5 History API
        //$locationProvider.html5Mode(true);
        //problema do refresh http://stackoverflow.com/questions/16569841/angularjs-html5-mode-reloading-the-page-gives-wrong-get-request?lq=1
  });
