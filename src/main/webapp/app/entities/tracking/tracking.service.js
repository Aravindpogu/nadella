(function() {
    'use strict';
    angular
        .module('nadellaApp')
        .factory('Tracking', Tracking);

    Tracking.$inject = ['$resource', 'DateUtils'];

    function Tracking ($resource, DateUtils) {
        var resourceUrl =  'api/trackings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.order_start_date = DateUtils.convertLocalDateFromServer(data.order_start_date);
                        data.order_end_date = DateUtils.convertLocalDateFromServer(data.order_end_date);
                        data.actual_delivery_date = DateUtils.convertLocalDateFromServer(data.actual_delivery_date);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.order_start_date = DateUtils.convertLocalDateToServer(data.order_start_date);
                    data.order_end_date = DateUtils.convertLocalDateToServer(data.order_end_date);
                    data.actual_delivery_date = DateUtils.convertLocalDateToServer(data.actual_delivery_date);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.order_start_date = DateUtils.convertLocalDateToServer(data.order_start_date);
                    data.order_end_date = DateUtils.convertLocalDateToServer(data.order_end_date);
                    data.actual_delivery_date = DateUtils.convertLocalDateToServer(data.actual_delivery_date);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
