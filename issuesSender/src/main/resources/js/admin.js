window.onload = function() {
  var EmailInputCreateView = AJS.RestfulTable.CustomCreateView.extend({
    render: function (self) {
      console.log(self);
      var $input = $('<input type="email" name="email" required/>');
      return $input;
    }
    });

  var CronInputCreateView = AJS.RestfulTable.CustomCreateView.extend({
    render: function (self) {
      console.log(self);
      var pat = "(\\*|([1-5]?\\d((-|\\/)[1-5]?\\d)?(,[1-5]?\\d((-|\\/)[1-5]?\\d)?)*)) "+
                "(\\*|([1-5]?\\d((-|\\/)[1-5]?\\d)?(,[1-5]?\\d((-|\\/)[1-5]?\\d)?)*)) "+
                "(\\*|(((1?\\d)|(2[0-3]))((-|\\/)((1?\\d)|(2[0-3])))?(,((1?\\d)|(2[0-3]))((-|\\/)((1?\\d)|(2[0-3])))?)*)) "+
                "(\\*|\\?|L|(([12]?\\d)|(3[01])W)|((([12]?\\d)|(3[01]))((-|\\/)(([12]?\\d)|(3[01])))?(,(([12]?\\d)|(3[01]))((-|\\/)(([12]?\\d)|(3[01])))?)*)) "+
                "(\\*|(((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC))(-((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)))?(,((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC))(-((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)))?)*)|"+
                "((([1-9])|(1[0-2]))((-|\\/)(([1-9])|(1[0-2])))?(,(([1-9])|(1[0-2]))((-|\\/)(([1-9])|(1[0-2])))?)*)) "+
                "(\\*|\\?|([1-7]L)|([1-7](((-|\\/)[1-7])|#[1-5])?(,[1-7](((-|\\/)[1-7])|#[1-5])?)*)|"+
                "(((MON)|(TUE)|(WEN)|(THU)|(FRI)|(SAT)|(SUN))L)|(((MON)|(TUE)|(WEN)|(THU)|(FRI)|(SAT)|(SUN))((-((MON)|(TUE)|(WEN)|(THU)|(FRI)|(SAT)|(SUN)))|#[1-5])?(,((MON)|(TUE)|(WEN)|(THU)|(FRI)|(SAT)|(SUN))((-(MON)|(TUE)|(WEN)|(THU)|(FRI)|(SAT)|(SUN))|#[1-5])?)*))"+
                "( (\\*|([12]\\d\\d\\d((-|\\/)[12]\\d\\d\\d)?(,[12]\\d\\d\\d((-|\\/)[12]\\d\\d\\d)?)*)|\\?))? ?";
      var $input = $('<input type="text" name="cron" required pattern=\"'+pat+'\"/>');
      return $input;
    }
  });
  var DisabledInputCreateView = AJS.RestfulTable.CustomCreateView.extend({
    render: function (self) {
      console.log(self);
      var $input = $('<input disabled/>');
      return $input;
    }
  });

  new AJS.RestfulTable({
    autoFocus: true,
    el: jQuery("#issues-tasks-table"),
    allowReorder: true,
    allowEdit: false,
    resources: {
      all: AJS.contextPath()+"/rest/issues-sender-rest/1.0/tasks/",
        self: AJS.contextPath()+"/rest/issues-sender-rest/1.0/tasks/"
      },
    noEntriesMsg: "There are currently no tasks.",
    columns: [
      {
        id: "email",
        header: "Email",
        createView: EmailInputCreateView
      },
      {
        id: "cron",
        header: "Cron",
        createView: CronInputCreateView
      },
      {
        id: "lastSend",
        header: "Last send",
        createView: DisabledInputCreateView,
        emptyText: "No date"
      },
      {
        id: "nextSend",
        header: "Next send",
        createView: DisabledInputCreateView,
        emptyText: "No date"
      }
    ]
  });
};