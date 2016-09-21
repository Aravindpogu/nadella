(function() {
    'use strict';

    angular
        .module('nadellaApp')
        .controller('ItemDetailController', ItemDetailController);

    ItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Item', 'Order'];

    function ItemDetailController($scope, $rootScope, $stateParams, previousState, entity, Item, Order) {
        var vm = this;

        vm.item = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('nadellaApp:itemUpdate', function(event, result) {
            vm.item = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
