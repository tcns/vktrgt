/**
 * Created by root on 3/14/16.
 */
angular.module('vktrgtApp')
    .factory('VKService', function ($rootScope, $timeout, $cookies, $window, $http, $q, CacheService) {
        var PREFIX = "https://api.vk.com/method/";
        var VERSION_PARAM = "5.59";
        var VERSION = "&v=" + VERSION_PARAM;
        var TOKEN_FIELD = "vk_token";
        var TOKEN_FIELD_STANDALONE = "vk_token_standalone";
        return {
            inviteUsers: function (users, groupId, scope, t) {
                var context = this;
                var count = 0;
                $http.jsonp(PREFIX + "groups.invite", {
                    params: {
                        group_id: groupId,
                        user_id: users[0],
                        access_token: t,
                        callback: 'JSON_CALLBACK',
                        v: VERSION_PARAM
                    }
                }).then(function (resp) {
                    if (resp.data.error && resp.data.error['error_code'] === 5) {
                        context.workaroundVkError(scope, null, 403, function () {
                        }, true)
                    } else {
                        context.setTokenStandalone(t);
                        for (var i in users) {
                            //context.sleep(2000);
                            var userId = users[i];
                            $http.jsonp(PREFIX + "groups.invite", {
                                params: {
                                    group_id: groupId,
                                    user_id: userId,
                                    access_token: t,
                                    callback: 'JSON_CALLBACK',
                                    v: VERSION_PARAM
                                }
                            }).then(function (r) {
                                context.sleep(20000);
                                if (r.data.error) {
                                    if (r.data.error['error_code'] === 6 || r.data.error['error_code'] === 14){
                                        context.sleep(60000);
                                    }
                                    console.log(r.data.error.error_msg);
                                } else {
                                    count++;
                                    scope.message = "Приглашено " + count;
                                }
                            })
                        }
                    }

                })
            },
            getUserAudio: function (userId, scope) {
                "use strict";
                var deferred = $q.defer();
                var context = this;
                var token = this.getToken();
                var getAudio = function (t) {
                    $http.jsonp(PREFIX + "audio.get", {
                        params: {
                            owner_id: userId,
                            count: 6000,
                            offset: 0,
                            access_token: t,
                            callback: 'JSON_CALLBACK',
                            v: VERSION_PARAM
                        }
                    }).then(function (r) {
                        if (r.data.error && r.data.error['error_code'] === 5) {
                            context.workaroundVkError(scope, null, 403, function () {
                            })
                            deferred.reject();
                        } else {
                            scope.message = "";
                            if (r.data.response) {
                                deferred.resolve({
                                    user: userId,
                                    items: r.data.response.items
                                });
                            } else {
                                console.log(r.data.error.error_msg);
                                deferred.resolve({});
                            }

                        }
                    })
                }
                token.then(function () {
                    context.getToken().then(function (t) {
                        context.sleep(1000);
                        getAudio(t);
                    })
                })
                return deferred.promise;
            },
            sleep: function sleep(ms) {
                ms += new Date().getTime();
                while (new Date() < ms) {
                }
            },
            searchAudio: function (users, audio, scope) {
                "use strict";
                var deferred = $q.defer();
                var context = this;
                var searchInner = function (i, response) {
                    context.getUserAudio(users[i], scope).then(function (userAudio) {
                        var p = i / users.length * 100;
                        scope.message = "Выполнено " + (p.toFixed(2)) + "%"
                        if (userAudio.items && userAudio.user) {
                            var count = 0;
                            for (var k in userAudio.items) {
                                var a = userAudio.items[k];
                                for (var j in audio) {
                                    var audioToSearch = audio[j].toLowerCase();
                                    if (a.artist.toLowerCase().indexOf(audioToSearch) > -1) {
                                        count++;
                                    }

                                }
                            }
                            response[userAudio.user] = count;
                        }
                        if (i < users.length - 1) {
                            searchInner(i + 1, response);
                        } else {
                            scope.message = "Выполнено 100%"
                            var sorted = context.getSortedKeys(response);
                            deferred.resolve(sorted);
                        }

                    })
                }
                this.getUserAudio(users[0], scope).then(function () {
                    var response = [];
                    searchInner(0, response);

                })

                return deferred.promise;
            },
            export: function (str, name) {
                "use strict";
                var element = document.createElement('a');
                element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(str));
                element.setAttribute('download', name + ".txt");
                element.style.display = 'none';
                document.body.appendChild(element);
                element.click();

            }, getSortedKeys: function (obj) {
                var keys = [];
                for (var key in obj) keys.push(key);
                return keys.sort(function (a, b) {
                    return obj[b] - obj[a]
                });
            },
            authorize: function (standalone) {
                if (standalone) {
                    if (!$cookies.get(TOKEN_FIELD_STANDALONE)) {
                        $window.open(
                            'https://oauth.vk.com/authorize?' +
                            'client_id=5674086&' +
                            'redirect_uri=https://oauth.vk.com/blank.html&' +
                            'scope=audio,groups&' +
                            //               'display=popup&'+
                            'response_type=token' +
                            // 'test_redirect_uri=1&'+
                            VERSION
                        );
                    }
                } else {
                    if (!$cookies.get(TOKEN_FIELD)) {
                        $window.open(
                            'https://oauth.vk.com/authorize?' +
                            'client_id=5574216&' +
                            'redirect_uri=' + location.protocol + '//' + window.location.host + '/Callback&' +
                            'scope=audio,groups&' +
                            //               'display=popup&'+
                            'response_type=code' +
                            // 'test_redirect_uri=1&'+
                            VERSION
                        );
                    }
                }

            },
            verificate: function (redirectUri) {
                $window.open(redirectUri);
            },
            workaroundVkError: function (scope, response, status, headers, standalone) {
                if (response == null || status === 403) {
                    if (headers('Errorcode') == '17') {
                        this.verificate(headers('Redirecturi'));
                        scope.message = "Вы не прошли верификацию, попробуйте еще раз после верификации";
                    } else {
                        this.authorize(standalone);
                        scope.message = "Вы неавторизованы, попробуйте еще раз после авторизации";
                    }

                } else {
                    scope.message = "Произошла ошибка";
                }

            },
            getUsersFromGroup: function (id, from, count) {
                var users = $http.jsonp(PREFIX + "groups.getMembers", {
                    params: {
                        group_id: id,
                        offset: from,
                        count: count,
                        callback: 'JSON_CALLBACK'
                    }
                })
                return users;
            },
            getUserCountsFromGroups: function (groups) {
                var queries = [];
                for (var i in groups) {
                    queries.push(this.getUsersFromGroup(groups[i], 0, 1));
                }
                return $q.all(queries);
            },
            getAllUsersFromGroup: function (id, userCount) {
                var queries = [];
                for (var i = 0; i < userCount; i += 1000) {
                    queries.push(this.getUsersFromGroup(id, i, 1000));
                }
                return $q.all(queries);
            },
            getAllUsersFromGroups: function (groups) {
                var defer = $q.defer();
                var context = this;
                this.getUserCountsFromGroups(groups).then(function (r) {
                    var all = [];
                    for (var i in r) {
                        var resp = r[i];
                        var count = resp.data.response.count;
                        var group = groups[i];
                        all.push(context.getAllUsersFromGroup(group, count));
                    }
                    defer.resolve(all);
                });
                return defer.promise;
            }, getCountries: function () {
                var cache = CacheService.get('Countries');
                var deferred = $q.defer();
                if (cache) {
                    deferred.resolve(cache);
                } else {
                    $http.jsonp(PREFIX + "database.getCountries?need_all=0&callback=JSON_CALLBACK&count=100" + VERSION, {
                        cache: true
                    }).then(function (o) {
                        CacheService.put('Countries', o.data.response.items)
                        deferred.resolve(o.data.response.items);
                    })
                }
                return deferred.promise;

            }, getCities: function (q) {
                var cache = null// CacheService.get('Cities');
                var deferred = $q.defer();
                if (cache) {
                    deferred.resolve(cache);
                } else {
                    $http.jsonp(PREFIX + "database.getCities?country_id=1&need_all=1&callback=JSON_CALLBACK&q=" + q + VERSION, {
                        cache: true
                    }).then(function (o) {
                        CacheService.put('Cities', o.data.response.items)
                        for (var i in o.data.response.items) {
                            if (o.data.response.items[i].area) {
                                o.data.response.items[i].title = o.data.response.items[i].title
                                    + " " + o.data.response.items[i].area
                            }
                        }

                        deferred.resolve(o.data.response.items);
                    })
                }
                return deferred.promise;

            }, getCitiesApiUrl: function () {
                return PREFIX + "database.getCities?country_id=1&need_all=1&callback=JSON_CALLBACK&count=100" + VERSION;

            },
            getToken: function (force) {
                "use strict";
                var token = CacheService.get(TOKEN_FIELD);
                var deferred = $q.defer();
                if (token && token.length > 1 && !force) {
                    deferred.resolve(token)
                } else {
                    return $http.get('/api/vk/token').then(function (o) {
                        token = o.data.token;
                        CacheService.put(TOKEN_FIELD, token);
                        deferred.resolve(token)
                    });
                }
                return deferred.promise;

            },
            getTokenStandalone: function () {
                "use strict";
                var token = CacheService.get(TOKEN_FIELD_STANDALONE);
                if (!token) {
                    return "";
                }

            },
            setTokenStandalone: function (token) {
                CacheService.put(TOKEN_FIELD_STANDALONE, token);

            }

        }

    })
