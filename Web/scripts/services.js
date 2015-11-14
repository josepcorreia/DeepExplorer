angular.module('deepApp.services', []).

factory('sharedInfo', function () {
        //measures avaiable in the system
        var measures = ['Dice','LogDice','PMI', 'ChiPearson', 'LogLikelihood', 'Significance','Frequency'];
        var word = "";
        var pos = "";
        var measure = "";
        var minfreq ="";
        var maxwords = "";
        var deps;

        return {
            getWord: function () {
                return word;
            },
            getPOS: function () {
                return pos;
            },
            getDatabasePOS: function () {
                //convert the pos to the db format
                var posHash = new Array();
                        posHash['Noun'] = 'NOUN';
                        posHash['Verb'] = 'VERB';
                        posHash['Adjective'] = 'ADJ';
                        posHash['Adverb'] = 'ADV';

                return  posHash[pos];
            },
            getNormalPOS: function (value) {
                //convert the db pos to the web format
                var posHash = new Array();
                    posHash['NOUN'] = 'Noun';
                    posHash['VERB'] = 'Verb';
                    posHash['ADJ'] = 'Adjective';
                    posHash['ADV'] = 'Adverb';

                return posHash[value];
            },
            getMeasure: function () {
                return measure;
            },
            getMeasures: function () {
                return measures;
            },
            getDeps: function () {
                return deps;
            },
            getMaxWords: function(){
              return maxwords;
            },
            getMinFreq: function(){
             return minfreq;
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
            },
            setMaxWords:function(value){
               maxwords = value; 
            },
            setMinFreq:function(value){
              minfreq = value;
            }
        };
}).

factory('postPHPservice', function (sharedInfo, $location, $rootScope, $route) {
            return {
                postWordPhp:function(request) {
                    $("body").css('overflow', 'hidden');
                    $("#waitForWord").show();

                    var word = sharedInfo.getWord();
                    var pos = sharedInfo.getDatabasePOS();
                    var measure = sharedInfo.getMeasure();
                    var limit = sharedInfo.getMaxWords();
                    var minfreq = sharedInfo.getMinFreq();

                    var data = {
                                'request_type':'word',
                                'word': word,
                                'pos': pos,
                                'measure':measure,
                                'limit':limit,
                                'minfreq':minfreq
                      };

                    var url = 'server-side/deepApp.php';            
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
                        if(response.hasOwnProperty("wordNotExist")){
                          $("#waitForWord").hide();
                          alert("The lemma " +word+ "("+sharedInfo.getNormalPOS(pos)+") does not exist in the database");
                        }              
                        else{
                          if(jQuery.isEmptyObject(response)){
                                $("#waitForWord").hide();
                                alert("No results have been found for the lemma  " +word+ "("+sharedInfo.getNormalPOS(pos)+")  with the selected options");
                          }
                          else{
                            sharedInfo.setDeps(response);
                            
                            $("body").css('overflow', 'scroll');
                            $("#waitForWord").hide(); 

                            var destinyURL = "/wordexplorer";
                            
                            if($location.url() == destinyURL){
                                $route.reload();  
                            } 
                            else {
                                $location.path(destinyURL);
                            }
                            $rootScope.$apply();
                          }
                        } 
                     });//request done

                    // Callback handler that will be called on failure
                    request.fail(function (jqXHR, textStatus, errorThrown){
                      alert("Error, try again!");
                    //return false;
                    console.error(
                        "The following error occurred: "+
                        textStatus, errorThrown
                    );
                    });//fail
                }//postPhp
            };
});

        