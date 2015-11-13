angular.module('deepApp.services', []).

service('sharedInfo', function () {
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
            getPos: function () {
                return pos;
            },
            getMeasure: function () {
                return measure;
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
});