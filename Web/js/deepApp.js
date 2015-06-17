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
            when('/verbo', {
                templateUrl : 'partials/verbo.html',
                controller  : 'nameCtrl'
            }).
             when('/adverbio', {
                templateUrl : 'partials/adverbio.html',
                controller  : 'nameCtrl'
            }).
            when('/adjectivo', {
                templateUrl : 'partials/adjectivo.html',
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
        var measure = "";
        var deps;
        return {
            getWord: function () {
                return word;
            },
            getPos: function () {
                return pos;
            },
            getMeasure: function () {
                return measure;
            },
            getDeps: function () {
                return deps;
            },
            setWord: function(value) {
                word = value;
            },
            setPos: function(value) {
                pos = value;
            },
            setMeasure: function(value) {
                measure = value;
            },
            setDeps: function(value) {
                deps = value;
            }
            
        };
});

deepApp.controller("searchCtrl", function($scope, sharedInfo, $location) {
   $scope.classes=['Nome','Verbo','Adjectivo','Advérbio']
   $scope.pos = "Classe";
   $scope.measures=['PMI','Dice','LogDice', 'Frequência']
   $scope.measure = "Medida";

   var posHash = new Array();
    posHash['Nome'] = 'NOUN';
    posHash['Verbo'] = 'VERB';
    posHash['Adjectivo'] = 'ADJ'; 
    posHash['Advérbio'] = 'ADV'; 

    var urlHash = new Array();
    urlHash['Nome'] = 'nome';
    urlHash['Verbo'] = 'verbo';
    urlHash['Adjectivo'] = 'adjectivo'; 
    urlHash['Advérbio'] = 'adverbio'; 

   $scope.changePos = function(value) {
     $scope.pos = value;
   };
   $scope.changeMeasure = function(value) {
     $scope.measure = value;  
   };
  
  var selectPath = function (){
    var path = "/" + urlHash[$scope.pos];

    $location.path(path);
  };

   $scope.searchWord = function() {
    
      if(angular.isString($scope.word)){
        if($scope.pos != "Classe"){
          if($scope.measure != "Medida"){
            sharedInfo.setWord($scope.word);
            sharedInfo.setPos($scope.pos);
            sharedInfo.setMeasure($scope.measure);

            $scope.postPhp()
          }
          else{
            alert("Selecionar qual a Medida a usar");
          }
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
                    'pos':posHash[$scope.pos],
                    'measure':$scope.measure
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
        });//request
        
          // Callback handler that will be called on success
        request.done(function (response, textStatus, jqXHR){
            sharedInfo.setDeps(response.DEPS);
            console.log(response);
            selectPath();
            $scope.$apply()
         });//request done

        // Callback handler that will be called on failure
        request.fail(function (jqXHR, textStatus, errorThrown){
        // Log the error to the console
        //return false;
        console.error(
            "The following error occurred: "+
            textStatus, errorThrown
        );
        });//fail
    }//postPhp
});//controller

deepApp.controller("nameCtrl", function($scope, sharedInfo) {
    var Deps = sharedInfo.getDeps();
    $scope.word = sharedInfo.getWord();
    $scope.pos =  sharedInfo.getPos();
    
    $scope.SUBJ = Deps.SUBJ;
    $scope.CDIR = Deps.CDIR;
    $scope.MOD = Deps.MOD;
    $scope.ATTRIB = Deps.ATTRIB;

    //console.log($scope.MOD);
    
    //console.log( Deps);
});