var app = angular.module("deepApp", []);

app.controller("searchCtrl", function($scope) {
   $scope.search = function() {alert("ddd");};
   $scope.palavra = "";
   $scope.classe = ""; 
});