(function() {
    'use strict';

    angular
        .module('nadellaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('travel', {
            parent: 'entity',
            url: '/travel?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Travels'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/travel/travels.html',
                    controller: 'TravelController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('travel-detail', {
            parent: 'entity',
            url: '/travel/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Travel'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/travel/travel-detail.html',
                    controller: 'TravelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Travel', function($stateParams, Travel) {
                    return Travel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'travel',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('travel-detail.edit', {
            parent: 'travel-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/travel/travel-dialog.html',
                    controller: 'TravelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Travel', function(Travel) {
                            return Travel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('travel.new', {
            parent: 'travel',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/travel/travel-dialog.html',
                    controller: 'TravelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                from_place: null,
                                to_place: null,
                                from_date: null,
                                to_date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('travel', null, { reload: true });
                }, function() {
                    $state.go('travel');
                });
            }]
        })
        .state('travel.edit', {
            parent: 'travel',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/travel/travel-dialog.html',
                    controller: 'TravelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Travel', function(Travel) {
                            return Travel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('travel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('travel.delete', {
            parent: 'travel',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/travel/travel-delete-dialog.html',
                    controller: 'TravelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Travel', function(Travel) {
                            return Travel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('travel', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
