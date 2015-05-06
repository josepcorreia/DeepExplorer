var deepApp = angular.module("deepApp", ['ngRoute']);


deepApp.config(function($routeProvider, $locationProvider) {
      	

 		$routeProvider.
            // route for the home page
            when('/', {
                templateUrl : 'partials/home.html',
                controller  : 'searchCtrl'
            }).

            // route for the about page
            when('/nome', {
                templateUrl : 'partials/nome.html',
                controller  : 'nameCtrl'
            }).

            otherwise({
            	redirectTo: '/'
        	});

        // use the HTML5 History API
      	//$locationProvider.html5Mode(true);
      	//problema do refresh http://stackoverflow.com/questions/16569841/angularjs-html5-mode-reloading-the-page-gives-wrong-get-request?lq=1
  });
       
    

deepApp.service('sharedInfo', function () {
        var word = "";
        var pos = "";

        return {
            getWord: function () {
                return word;
            },
            getPos: function () {
                return word;
            },
            setWord: function(value) {
                word = value;
            },
            setPos: function(value) {
                pos = value;
            }
            
        };
    });

deepApp.controller("searchCtrl", function($scope, sharedInfo, $location) {
   $scope.pos = "Classe"; 

   $scope.changePos = function(value) {
		 $scope.pos = value;
	};

   $scope.searchWord = function() {
   		if(angular.isString($scope.word)){
   			if($scope.pos != "Classe"){
   				sharedInfo.setWord($scope.word);
   				sharedInfo.setPos( $scope.pos);

				$location.path("/nome");

   			}else{
   				alert("Selecionar qual a classe da palavra");
   			}
   		}else{
   			alert("Introduzir uma palavra");
   		}
   };
   
   $scope.classes=['Nome','Verbo','Adjectivo','Advérbio']
});

deepApp.controller("nameCtrl", function($scope, sharedInfo) {
	$scope.word = sharedInfo.getWord();



});