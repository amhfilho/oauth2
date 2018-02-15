var app = angular.module('myApp', ["ngResource","ngRoute","oauth"]);
app.config(function($locationProvider) {
  $locationProvider.html5Mode({
	  enabled: true,
	  requireBase: false
	}).hashPrefix('!');
});

app.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push(function ($q,$rootScope) {
        return {
            'responseError': function (responseError) {
                $rootScope.message = responseError.statusText;
                console.log("error here");
                console.log(responseError);
                return $q.reject(responseError);
            }
        };
    });
}]);

app.controller('mainCtrl', function($scope,$resource,$http,$rootScope) {
    $scope.$on('oauth:login', function(event, token) {
		$http.defaults.headers.common.Authorization= 'Bearer ' + token.access_token;
		console.log('Authorized third party app with token', token.access_token);
		$scope.token=token.access_token;
	});

});