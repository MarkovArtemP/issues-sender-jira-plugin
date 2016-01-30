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
                    var $select = $('<input type="text" name="cron" required/>');
                    return $select;
                }
            });

    var CronInputCreateView = AJS.RestfulTable.CustomCreateView.extend({
                render: function (self) {
                    console.log(self);
                    var $select = $('<input type="text" name="cron" required/>');
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
                header: AJS.I18n.getText("issuesSender.admin.email.label"),
                editView: EmailInputEditView,
                createView: EmailInputCreateView,
            },
            {
                id: "cron",
                header: AJS.I18n.getText("issuesSender.admin.cron.label"),
                editView: CronInputEditView,
                createView: CronInputCreateView,
            }
        ]
    });
};