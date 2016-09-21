(function() {
    'use strict';

    angular
        .module('nadellaApp')
        .controller('TrackingDialogController', TrackingDialogController);

    TrackingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Tracking', 'Order', 'Travel'];

    function TrackingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Tracking, Order, Travel) {
        var vm = this;

        vm.tracking = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.trackings = Order.query({filter: 'tracking-is-null'});
        $q.all([vm.tracking.$promise, vm.trackings.$promise]).then(function() {
            if (!vm.tracking.tracking || !vm.tracking.tracking.id) {
                return $q.reject();
            }
            return Order.get({id : vm.tracking.tracking.id}).$promise;
        }).then(function(tracking) {
            vm.trackings.push(tracking);
        });
        vm.travels = Travel.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tracking.id !== null) {
                Tracking.update(vm.tracking, onSaveSuccess, onSaveError);
            } else {
                Tracking.save(vm.tracking, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nadellaApp:trackingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.order_start_date = false;
        vm.datePickerOpenStatus.order_end_date = false;
        vm.datePickerOpenStatus.actual_delivery_date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
