var app = angular.module('chatApp', ['ui.router', 'ngWebSocket']);

app.constant('urls', {
    USER_SERVICE_API : 'http://localhost:8080/SpringBootCRUDApp/api/user/',

});

app.config(['$stateProvider', '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {
        console.log("usao da namesti state");
        $stateProvider
            .state('home', {
                url: '/home',
                views: {
                    '@': {
                        templateUrl: '/ChatAppWar/templates/home.html',
                        controller: 'HomeController',
                        controllerAs: 'homeCtrl'
                    }
                }
            });
            $urlRouterProvider.otherwise('/');
    }
]);