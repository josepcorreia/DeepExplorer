angular.module('deepApp.services', []).

service('sharedInfo', function () {
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
service('scopeApplyService', function() {
     return {
         safeApply: function ($scope, fn) {
             var phase = $scope.$root.$$phase;
             if (phase == '$apply' || phase == '$digest') {
                 if (fn && typeof fn === 'function') {
                     fn();
                 }
             } else {
                 $scope.$apply(fn);
             }
         },
     };
});

        