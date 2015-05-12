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
        var mod;
        return {
            getWord: function () {
                return word;
            },
            getPos: function () {
                return pos;
            },
            getMod: function () {
                return mod;
            },
            setWord: function(value) {
                word = value;
            },
            setPos: function(value) {
                pos = value;
            },
            setMod: function(value) {
                mod = value;
            }
            
        };
    });

deepApp.controller("searchCtrl", function($scope, sharedInfo, $location) {
   $scope.classes=['Nome','Verbo','Adjectivo','Advérbio']
   $scope.pos = "Classe"; 

   $scope.changePos = function(value) {
		 $scope.pos = value;
	};

   $scope.searchWord = function() {
   		if(angular.isString($scope.word)){
   			if($scope.pos != "Classe"){
   				sharedInfo.setWord($scope.word);
   				sharedInfo.setPos($scope.pos);
          $scope.pos = "VERB";
          $scope.postPhp();
          $location.path("/nome");
   			}
        else{
   				alert("Selecionar qual a classe da palavra");
   			}
   		}else{
   			alert("Introduzir uma palavra");
   		}
   };
    $scope.postPhp = function() {
       var url = 'php/conn.php';
       var data = {
                    'word':$scope.word,
                    'pos':$scope.pos
          };
        // Variable to hold request
        var request;
        // Abort any pending request
        if (request) {
          request.abort();
        }
        request = $.ajax({
                    type: 'POST',
                    url: url,
                    dataType: "json",
                    data: data,
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    async: true
        });
        
          // Callback handler that will be called on success
        request.done(function (response, textStatus, jqXHR){
            sharedInfo.setMod(response.Mod);
            console.log(response.Mod);
         });

        // Callback handler that will be called on failure
        request.fail(function (jqXHR, textStatus, errorThrown){
        // Log the error to the console
        console.error(
            "The following error occurred: "+
            textStatus, errorThrown
        );
    });

    }
      
   
});

deepApp.controller("nameCtrl", function($scope, sharedInfo) {
    $scope.Mod = sharedInfo.getMod();
    $scope.word = sharedInfo.getWord();
    $scope.pos = "VERB";//sharedInfo.getPos();
    
    console.log($scope.Mod);
  

});