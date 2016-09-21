(function() {
    'use strict';

    angular
        .module('nadellaApp')
        .controller('TravelDialogController', TravelDialogController);

    TravelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Travel', 'Tracking'];

    function TravelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Travel, Tracking) {
        var vm = this;

        vm.travel = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.trackings = Tracking.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.travel.id !== null) {
                Travel.update(vm.travel, onSaveSuccess, onSaveError);
            } else {
                Travel.save(vm.travel, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nadellaApp:travelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.from_date = false;
        vm.datePickerOpenStatus.to_date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
