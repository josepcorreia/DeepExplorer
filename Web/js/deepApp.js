var deepApp = angular.module("deepApp", ['ngRoute']);


deepApp.config(function($routeProvider, $locationProvider) {

    $routeProvider.
            // route for the home page
            when('/', {
                templateUrl : 'partials/home.html',
                controller  : 'searchCtrl'
            }).

            // route for the about page
            when('/deepexplorer', {
                templateUrl : 'partials/deep.html',
                controller  : 'deepCtrl'
            }).
            when('/nome', {
                templateUrl : 'partials/nome.html',
                controller  : 'nounCtrl'
            }).
            when('/verbo', {
                templateUrl : 'partials/verbo.html',
                controller  : 'verbCtrl'
            }).
             when('/adverbio', {
                templateUrl : 'partials/adverbio.html',
                controller  : 'advCtrl'
            }).
            when('/adjetivo', {
                templateUrl : 'partials/adjetivo.html',
                controller  : 'adjCtrl'
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
   $scope.classes=['Nome','Verbo','Adjetivo','Advérbio']
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
       var url = 'php/deep.php';
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
            $location.path("/nome");
            $location.path("/deepexplorer");
           // selectPath();
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

deepApp.controller("nounCtrl", function($scope, sharedInfo) {
    var Deps = sharedInfo.getDeps();
    $scope.word = sharedInfo.getWord();
    $scope.pos =  sharedInfo.getPos();

    $scope.MOD_PRE_NOUN_ADV = Deps.MOD_PRE_NOUN_ADV;
    $scope.MOD_PRE_NOUN_ADJ = Deps.MOD_PRE_NOUN_ADJ;
    $scope.MOD_POST_NOUN_PP = Deps.MOD_POST_NOUN_PP;
    $scope.MOD_POST_NOUN_ADJ = Deps.MOD_POST_NOUN_ADJ;

    //console.log( Deps);
});

deepApp.controller("verbCtrl", function($scope, sharedInfo) {
    var Deps = sharedInfo.getDeps();
    $scope.word = sharedInfo.getWord();
    $scope.pos =  sharedInfo.getPos();

    $scope.SUBJ_SEM_PROP = Deps.SUBJ_SEM_PROP;
    $scope.CDIR_SEM_PROP = Deps.CDIR_SEM_PROP;
    $scope.COMPLEMENTOS_SEM_PROP = Deps.COMPLEMENTOS_SEM_PROP;
    $scope.MOD_VERB_ADV = Deps.MOD_VERB_ADV;

    //console.log( Deps);
});

deepApp.controller("advCtrl", function($scope, sharedInfo) {
    var Deps = sharedInfo.getDeps();
    $scope.word = sharedInfo.getWord();
    $scope.pos =  sharedInfo.getPos();

    $scope.MOD_PRE_NOUN_ADV = Deps.MOD_PRE_NOUN_ADV;
    $scope.MOD_VERB_ADV = Deps.MOD_VERB_ADV;
    $scope.MOD_PRE_ADJ_ADV = Deps.MOD_PRE_ADJ_ADV;
    $scope.MOD_ADV_ADV = Deps.MOD_ADV_ADV;
     $scope.MOD_TOP_ADV = Deps.MOD_TOP_ADV;

    //console.log( Deps);
});

deepApp.controller("adjCtrl", function($scope, sharedInfo) {
    var Deps = sharedInfo.getDeps();
    $scope.word = sharedInfo.getWord();
    $scope.pos =  sharedInfo.getPos();

    $scope.MOD_PRE_ADJ_ADJ = Deps.MOD_PRE_ADJ_ADJ;
    $scope.MOD_PRE_ADJ_ADV = Deps.MOD_PRE_ADJ_ADV;
    $scope.MOD_PRE_NOUN_ADJ = Deps.MOD_PRE_NOUN_ADJ;
    $scope.MOD_POST_NOUN_ADJ = Deps.MOD_POST_NOUN_ADJ;
    $scope.MOD_POST_ADJ_PP = Deps.MOD_POST_ADJ_PP;

    //console.log( Deps);
});
deepApp.controller("deepCtrl", function($scope, sharedInfo) {
    var Deps = sharedInfo.getDeps();
    $scope.word = sharedInfo.getWord();
    $scope.pos =  sharedInfo.getPos();
    
        //console.log( Deps);
  });
