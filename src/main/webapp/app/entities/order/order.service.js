(function() {
    'use strict';
    angular
        .module('nadellaApp')
        .factory('Order', Order);

    Order.$inject = ['$resource', 'DateUtils'];

    function Order ($resource, DateUtils) {
        var resourceUrl =  'api/orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.order_date = DateUtils.convertLocalDateFromServer(data.order_date);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.order_date = DateUtils.convertLocalDateToServer(data.order_date);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.order_date = DateUtils.convertLocalDateToServer(data.order_date);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
