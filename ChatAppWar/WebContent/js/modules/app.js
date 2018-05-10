var app = angular.module('chatApp', ['ui.router', 'ngWebSocket']);
app.run(function ($rootScope, $trace, $transitions) {
    //$trace.enable('TRANSITION');
    $rootScope.globals = {                
        currentUser: {                    
            username: undefined    
        }                          
    };
    $transitions.onStart({ to: 'home-abstract.register' }, function(trans) {
        console.log("Krenuo u register state.");
        console.log("RootScope:"+$rootScope.globals.username);
        if ($rootScope.globals.currentUser.username) {
          // User isn't authenticated. Redirect to a new Target State
          //return trans.router.stateService.target('login');
            $rootScope.globals = {};
        }
    });
});
app.constant('urls', {
    USER_SERVICE_API: 'http://localhost:8080/SpringBootCRUDApp/api/user/',

});

app.config(['$stateProvider', '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {
        console.log("usao da namesti state");
        $stateProvider
            .state('home-abstract', {
                abstract: true,
                views: {
                    '@': {
                        templateUrl: '/ChatAppWar/templates/home.html',
                        controller: 'HomeController',
                        controllerAs: 'homeCtrl'
                    }
                }
            })
            .state('home-abstract.register', {
                url: '/register',
                views: {
                    'register': {
                        templateUrl: '/ChatAppWar/templates/register.html',
                        /*  controller: 'RegisterController',
                          controllerAs: 'registerCtrl'*/
                    }
                }
            })
            .state('home-abstract.profile-abstract', {
                url: '/:username',
                abstract: true,
                views: {
                    'profile-abstract': {
                        templateUrl: '/ChatAppWar/templates/profile-abstract.html'
                    }
                }
            })
            .state('home-abstract.profile-abstract.profile-overview', {
                url: '/overview',
                views: {
                    'overview': {
                        templateUrl: '/ChatAppWar/templates/profile-overview.html',
                          controller: 'OverviewController',
                          controllerAs: 'overviewCtrl'
                    }
                }
            })
            .state('home-abstract.profile-abstract.friends-abstract', {
                abstract: true,
                views: {
                    'friends': {
                        templateUrl: '/ChatAppWar/templates/friends-abstract.html'
                    }
                }
            })
            .state('home-abstract.profile-abstract.friends-abstract.friends-list', {
                url: '/friendsList',
                views: {
                    'friends-list': {
                        templateUrl: '/ChatAppWar/templates/friends-list.html'
                    }
                }
            })
            .state('home-abstract.profile-abstract.friends-abstract.friends-search', {
                url: '/friendsSearch',
                views: {
                    'friends-search': {
                        templateUrl: '/ChatAppWar/templates/friends-abstract-search.html'
                    }
                }
            });
        $urlRouterProvider.otherwise('/register');
    }
]);