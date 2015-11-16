angular.module('deepApp.controllers', []).

controller("searchCtrl", function($scope, sharedInfo, postPHPservice, $location, $window, $route) {
   //changes in the nav bar
   $('#search').tab('show');

   // Variable to hold request (requet to php)
   var request;

   $scope.pos="Active";  

   $scope.pos = "POS";     
   $scope.classes=['Noun','Verb','Adjective','Adverb']
   
   $scope.measure = "Measure";
   $scope.measures = sharedInfo.getMeasures();
  
   //default values
   $scope.minfreq = 2;
   $scope.maxword = 10;

   $scope.changePos = function(value) {
     $scope.pos = value;
   };
   $scope.changeMeasure = function(value) {
     $scope.measure = value;
   };

   $scope.moreOptions = function() {
       $(".moreOptions").show();
       $(".showmoreOptionButtons").hide();
       $(".showlessOptionButtons").css("display","table");
       
   };

   $scope.lessOptions = function() {
       $(".moreOptions").hide();
       $(".showmoreOptionButtons").css("display","table");
       $(".showlessOptionButtons").hide();    
   };
  
  $scope.reloadHomePage = function() {
    // Abort any pending request
        if (request) {
          request.abort();
        }
    $route.reload();
  };

   $scope.searchWord = function() {

      if(angular.isString($scope.word)){
        if($scope.pos != "POS"){
          if($scope.measure != "Measure"){
            sharedInfo.setWord($scope.word);
            sharedInfo.setPos($scope.pos); 
            sharedInfo.setMeasure($scope.measure);
            sharedInfo.setMinFreq($scope.minfreq);
            sharedInfo.setMaxWords($scope.maxword);

            postPHPservice.postWordPhp(request);
          }
          else{
            $window.alert("Select the Association Measure");
          }
        }
        else{
          $window.alert("Select the Word Part Of Speech");
        }
      }else{
        $window.alert("Insert a Word's Lemma");
      }
   };
}).

controller("deepCtrl", function($scope, sharedInfo, postPHPservice, scopeApplyService, $route,  $location) {
  //changes in the nav bar
   $('#search').tab('show');

  var request;
  $scope.measures = sharedInfo.getMeasures();

  $scope.loadPRE_WORD = function(Deps){

    if(Deps.hasOwnProperty("PRE_WORD")){
      $scope.PRE_WORD = Deps.PRE_WORD;
      if(!jQuery.isPlainObject(Deps.PRE_WORD)){
        $scope.msg_1 ="Não se encontraram ocorrências";
        $( "#PRE_WORD" ).find('.notApplicable').show();
      }

    } 
  }
  $scope.loadPOST_WORD = function(Deps){

    if(Deps.hasOwnProperty("POST_WORD")){
      $scope.POST_WORD = Deps.POST_WORD;
      if(!jQuery.isPlainObject(Deps.POST_WORD)){
        $scope.msg_2 ="Não se encontraram ocorrências";
        $("#POST_WORD").find('.notApplicable').show();
      }
    }
  }

  $scope.loadPRE_VERB = function(Deps){

    if(Deps.hasOwnProperty("PRE_VERB")){
      $scope.PRE_VERB = Deps.PRE_VERB;
      $( ".Verb_content" ).show();
      if(!jQuery.isPlainObject(Deps.PRE_VERB)){
        $scope.msg_1 ="Não se encontraram ocorrências";
        $( "#PRE_VERB" ).find('.notApplicable').show();
      }
    }  
  }
  $scope.loadPOST_VERB = function(Deps){

    if(Deps.hasOwnProperty("POST_VERB")){
      $scope.POST_VERB = Deps.POST_VERB;
      $( ".Verb_content" ).show();
      if(!jQuery.isPlainObject(Deps.POST_VERB)){
        $scope.msg_2 ="Não se encontraram ocorrências";
        $("#POST_VERB").find('.notApplicable').show();
      }
    }
  }

  $scope.loadData = (function(){
    var Deps = sharedInfo.getDeps();

    if(!angular.isUndefined(Deps)){
      $scope.word = sharedInfo.getWord();
      $scope.pos =  sharedInfo.getPOS();
      $scope.measure =  sharedInfo.getMeasure();
      $scope.maxword =  sharedInfo.getMaxWords();
      $scope.minfreq =  sharedInfo.getMinFreq();

      $scope.title = $scope.word + ", " +$scope.pos;

      $scope.loadPRE_WORD(Deps);
      $scope.loadPOST_WORD(Deps);
      $scope.loadPRE_VERB(Deps);
      $scope.loadPOST_VERB(Deps);

      if($scope.pos == "Adverb"){
       if(Deps.hasOwnProperty("SENTENCE")){
          var freq = Deps.SENTENCE.MOD_TOP_ADV.data[0].frequency;
          if(freq > 0){
          $scope.top_count = freq;
          $("#ADV_EXTRA").show()
          }
        }      
      }
    } 
    else{
      scopeApplyService.safeApply($scope, function() {
        $location.path("/");
      });
    }

  });

  //load the co-occurrence data
  $scope.loadData();

  $scope.changeMeasureDeps = function(value) {
    $scope.measure = value;
    sharedInfo.setMeasure($scope.measure);

    postPHPservice.postWordPhp(request);
  };

  $scope.reloadPage = function() {
    // Abort any pending request
    if (request) {
      request.abort();
    }
    $("body").css('overflow', 'scroll');
    $("#waitForWord").hide();
    $scope.loadData();
  };

    $scope.gotoHomePage = function() {
      $location.path("/#/");
    };

    var postSentencesRequestPhp = function(reuqest, data){
      var url = 'server-side/deepApp.php';
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
            //console.log(response);
            $scope.sentences = response.sentences;
            $('#myModal').load();
            $scope.$apply();
            $("#waitForConcordance").hide();
      });//request done
        // Callback handler that will be called on failure
      request.fail(function (jqXHR, textStatus, errorThrown){
          $('#myModal').modal('hide'); 
          $("#waitForConcordance").hide();

            //return false;
            console.error(
              "The following error occurred: "+
              textStatus, errorThrown
              );
      });//fail 
    };

    //setence Examples
    $scope.loadCoocorrenceExample = (function(depProp, word_obj, elementType){
      $scope.sentences = null;
      $("#waitForConcordance").show();
      $('#myModal').modal('show'); 

      var type = depProp.dep_type;

      switch(type) {
        case 'GOVERNED':
            //title of modal
            $scope.exampleTitle = depProp.dep +"_"+ depProp.prop + " ("+ $scope.word +","+word_obj.word+")";
            
            var data = {
              'request_type':'concordance',
              'word1':$scope.word,
              'pos1':sharedInfo.getDatabasePOS(),
              'word2':word_obj.word,
              'pos2':word_obj.word_pos,
              'dep':depProp.dep,
              'prop':depProp.prop,
              'freq':word_obj.frequency
            };
        break;
        case 'GOVERNOR':
            //title of modal
            $scope.exampleTitle = depProp.dep +"_"+ depProp.prop + " ("+ word_obj.word +","+ $scope.word +")";
            
            var data = {
              'request_type':'concordance',
              'word1':word_obj.word,
              'pos1':word_obj.word_pos,
              'word2':$scope.word,
              'pos2':sharedInfo.getDatabasePOS(),
              'dep':depProp.dep,
              'prop':depProp.prop,
              'freq':word_obj.frequency
            };
            break; 
      }
      
      //request to the php side
      postSentencesRequestPhp(request, data); 

      //modal information
      $scope.otherWord = word_obj.word;
      $scope.pos_otherWord = sharedInfo.getNormalPOS(word_obj.word_pos);

      switch(elementType) {
          case 'PRE_WORD':
          $scope.depTitle = depProp.name + " on the left";
          break;
          case 'POST_WORD':
          $scope.depTitle = depProp.name + " on the right";
          break;
          case 'PRE_VERB':
          $scope.depTitle = depProp.name;
          break;
          case 'POST_VERB':
          $scope.depTitle = depProp.name;
          break;  
      }

      $scope.m_value = word_obj.measure;
      $scope.frequency = word_obj.frequency;
      $scope.logfrequency = word_obj.duallog;

      //This event is fired when the modal has finished being hidden from the user 
      $('#myModal').on('hidden.bs.modal', function (e) {
        if (request) {
          request.abort();
        }
      })
    });
}).
controller("aboutCtrl", function($scope) {
  //changes in the nav bar
   $('#about').tab('show');
}).  

controller("helpCtrl", function($scope) {
  //changes in the nav bar
   $('#help').tab('show');
});
