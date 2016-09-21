(function() {
    'use strict';

    angular
        .module('nadellaApp')
        .controller('OrderDetailController', OrderDetailController);

    OrderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Order', 'Item', 'Tracking'];

    function OrderDetailController($scope, $rootScope, $stateParams, previousState, entity, Order, Item, Tracking) {
        var vm = this;

        vm.order = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('nadellaApp:orderUpdate', function(event, result) {
            vm.order = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
