window.onload = function() {
    var EmailInputEditView = AJS.RestfulTable.CustomEditView.extend({
            render: function (self) {
                console.log(self);
                var $select = $('<input type="email" name="email" required/>');
                return $select;
            }
        });

    var EmailInputCreateView = AJS.RestfulTable.CustomCreateView.extend({
        render: function (self) {
            console.log(self);
            var $select = $('<input type="email" name="email" required/>');
            return $select;
        }
    });

    var CronInputEditView = AJS.RestfulTable.CustomEditView.extend({
        render: function (self) {
            console.log(self);
            var $select = $('<input type="text" name="cron" required pattern="(\\*|([1-5]?\\d(-[1-5]?\\d)?(,[1-5]?\\d(-[1-5]?\\d)?)*)) (\\*|(((1?\\d)|(2[0-3]))(-((1?\\d)|(2[0-3])))?(,((1?\\d)|(2[0-3]))(-((1?\\d)|(2[0-3])))?)*)) (\\*|((([12]?\\d)|(3[01]))(-(([12]?\\d)|(3[01])))?(,(([12]?\\d)|(3[01]))(-(([12]?\\d)|(3[01])))?)*)) (\\*|((([1-9])|(1[0-2]))(-(([1-9])|(1[0-2])))?(,(([1-9])|(1[0-2]))(-(([1-9])|(1[0-2])))?)*)) (\\*|([0-7](-[0-7])?(,[0-7](-[0-7])?)*)) ?"/>');
            return $select;
        }
    });

    var CronInputCreateView = AJS.RestfulTable.CustomCreateView.extend({
        render: function (self) {
            console.log(self);
            var $select = $('<input type="text" name="cron" required pattern="(\\*|([1-5]?\\d(-[1-5]?\\d)?(,[1-5]?\\d(-[1-5]?\\d)?)*)) (\\*|(((1?\\d)|(2[0-3]))(-((1?\\d)|(2[0-3])))?(,((1?\\d)|(2[0-3]))(-((1?\\d)|(2[0-3])))?)*)) (\\*|((([12]?\\d)|(3[01]))(-(([12]?\\d)|(3[01])))?(,(([12]?\\d)|(3[01]))(-(([12]?\\d)|(3[01])))?)*)) (\\*|((([1-9])|(1[0-2]))(-(([1-9])|(1[0-2])))?(,(([1-9])|(1[0-2]))(-(([1-9])|(1[0-2])))?)*)) (\\*|([0-7](-[0-7])?(,[0-7](-[0-7])?)*)) ?"/>');
            return $select;
        }
    });

    new AJS.RestfulTable({
        autoFocus: true,
        el: jQuery("#issues-tasks-table"),
        allowReorder: true,
        resources: {
            all: AJS.contextPath()+"/rest/issues-sender-rest/1.0/tasks/",
            self: AJS.contextPath()+"/rest/issues-sender-rest/1.0/tasks/"
        },
        noEntriesMsg: "There are currently no tasks.",
        columns: [
            {
                id: "email",
                header: "Email",
                createView: EmailInputCreateView,
                editView: EmailInputEditView
            },
            {
                id: "cron",
                header: "Cron",
                createView: CronInputCreateView,
                editView: CronInputEditView
            }
        ]
    });
};