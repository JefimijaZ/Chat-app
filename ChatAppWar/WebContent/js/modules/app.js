var app = angular.module('chatApp', ['ui.router', 'ngWebSocket']);
app.run(function ($rootScope, $trace, $transitions, UserService) {
    //$trace.enable('TRANSITION');
    window.addEventListener("beforeunload", function (e) {
        UserService.send("Logout,"+$rootScope.globals.currentUser.username);
        //Webkit, Safari, Chrome
      });
    
    $rootScope.globals = {                
        currentUser: {                    
            username: null
        }                          
    };
    $transitions.onStart({ to: 'home-abstract.register' }, function(trans) {
        if ($rootScope.globals.currentUser.username) {
          // User isn't authenticated. Redirect to a new Target State
          //return trans.router.stateService.target('login');
            $rootScope.globals = {};
        }
    });
    $rootScope.$on('$destroy', function() {
        console.log("Unistio se window");
        UserService.send("Logout,"+$rootScope.globals.currentUser.username);
     });
    $transitions.onBefore({}, function(transition) {
        // check if the state should be protected

        if (($rootScope.globals.currentUser.username == null || $rootScope.globals.currentUser.username == undefined) && transition.to().name !== 'home-abstract.register') {
          // redirect to the 'login' state
          return transition.router.stateService.target('home-abstract.register');
        }
      });
});
app.constant('urls', {
    USER_SERVICE_API: 'http://localhost:8080/SpringBootCRUDApp/api/user/',

});

app.config(['$stateProvider', '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {
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
                          controller: 'RegisterController',
                          controllerAs: 'registerCtrl'
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
                        templateUrl: '/ChatAppWar/templates/friends-abstract.html',
                        controller: 'FriendsAbstractController',
                        controllerAs: 'faCtrl'
                    }
                }
            })
            .state('home-abstract.profile-abstract.friends-abstract.friends-list', {
                url: '/friendsList',
                views: {
                    'friends-list': {
                        templateUrl: '/ChatAppWar/templates/friends-list.html',
                        controller: 'FriendsListController',
                        controllerAs: 'friendsCtrl'
                    }
                }
            })
            .state('home-abstract.profile-abstract.friends-abstract.friends-search', {
                url: '/friendsSearch',
                views: {
                    'friends-search': {
                        templateUrl: '/ChatAppWar/templates/friends-search.html',
                        controller: 'FriendsSearchController',
                        controllerAs: 'searchCtrl'
                    }
                },
                params: {
                    searchValue: null
                }
                
            })
            .state('home-abstract.inbox-abstract', {
                url:'/inbox',
                abstract: true,
                views:{
                    'inbox-abstract':{
                        templateUrl: '/ChatAppWar/templates/inbox-abstract.html',
                        controller: 'InboxController',
                        controllerAs: 'inboxCtrl'
                    }
                },
                params: {
                    username: null
                }
            })
            .state('home-abstract.inbox-abstract.chat', {
                url:'/inbox',
                views:{
                    'chatView':{
                        templateUrl: '/ChatAppWar/templates/chat.html',
                        controller: 'ChatController',
                        controllerAs: 'chatCtrl'
                    }
                },
                params: {
                    friendUsername: null
                }
            })
        $urlRouterProvider.otherwise('/register');
    }
]);