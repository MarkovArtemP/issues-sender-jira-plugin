<!-- Begin

/*#############################################

 File: $Name$
 Revision: $Revision: #1 $
 Modified: $DateTime: 2006/05/13 02:53:51 $

 Application for crontab.html

 Code When          Who               What
 ---- ------------- ----------------- -------------------------------------
 EP01 21-Apr-2006   edward@evpnet.com Initial version

#############################################*/
// Doc: http://wp.netscape.com/eng/mozilla/3.0/handbook/javascript/

// ***********************************
// Stack implementation class
// ***********************************
function crtStackGet(ind) {
	return this.st[ind];
}

function crtStackGetLast() {
	return this.get(this.st_ind);
}

function crtStackGetFirst() {
	return this.get(0);
}

function crtStackPush(v) {
	this.st_ind++; this.st[this.st_ind] = v;
}

function crtStackCount() {
	return this.st_ind + 1;
}

function crtStackReset() {
	this.st_ind = -1;
}

// Constructor
function clsStack(StackSize) {
	this.st = new Array(StackSize);
	this.st_ind = -1;
	this.get = crtStackGet;
	this.getLast = crtStackGetLast;
	this.getFirst = crtStackGetFirst;
	this.push = crtStackPush;
	this.count = crtStackCount;
	this.reset = crtStackReset;
}
// END OF Stack class

// ***********************************
// CrontabValue class
// ***********************************
function ctrPrintStack(st) {
	if ( st.count() == 1 ) { // if only 1 value
		return st.getLast();
	} else {
		if ( st.count() == 2 ) { // if 2 values use comma (,)
			return st.getFirst() + "," + st.getLast();
		} else { // if more than 2 values use dash (-)
			return st.getFirst() + "-" + st.getLast();
		}
	}
}

// Gets an Array of Selected values and returns
// a string of (example):
// *     - if the Array is empty
// 1,3-5 - if the Array has 1,3,4,5 values
function crtValuesToString() {
var ret = "";

	if ( this.values[0] == -1 ) return "*";

	var st = new clsStack(this.values.length);

	st.push(this.values[0]);

	for(var i=1; i < this.values.length; i++) {

		if (this.values[i] == -1) break;

		// is it NOT a consecutive value
		if ( (parseInt(st.getLast()) + 1) != parseInt(this.values[i]) ) {
			// print the stack
			ret += (ret == "") ? ctrPrintStack(st) : "," + ctrPrintStack(st);

			// reset the stack
			st.reset();
		}

		// put the current value to the stack
		st.push(this.values[i]);

	} // for

	// print the stack
	ret += (ret == "") ? ctrPrintStack(st) : "," + ctrPrintStack(st);

	return ret;
}

// Adds a value
function crtAddValue(v) {
var i = 0;
	// Find first not -1 element
	for(; i < this.values.length && this.values[i] != -1; i++);

	this.values[i] = v;
	i++;

	this.values[i] = -1;
}

// Adds a value
function crtResetValues() {
	this.values[0] = -1;
}

// Constructor
function clsCrontabValue(NumOfElements) {
	// Contain values for Days,...,WeekDays that were selected
	// by the user. A value of -1 is inserted after the last selected
	// element of the array
	this.values = new Array(NumOfElements);
	this.ValuesToString = crtValuesToString;
	this.AddValue = crtAddValue;
	this.ResetValues = crtResetValues;

	this.values[0] = -1;
}
// END OF Class CrontabValue

// **************************************************************
// Global vars
// Contain values for Days,...,WeekDays that were selected
// **************************************************************
var objDays = new clsCrontabValue(31);
var objMonths = new clsCrontabValue(12);
var objHours = new clsCrontabValue(24);
var objMinutes = new clsCrontabValue(60);
var objWeekDays = new clsCrontabValue(7);


// **************************************************************
// ***************** User Interface functions *******************
// **************************************************************

// Init
function InitCrontab() {
	document.getElementById("frmMain").Result.value = "";

	ClearAllCtls();
}

// Gets a Select object (from this page) and populates
function SelectionToValues(selObj, objCT) {
	for(var i=0; i < selObj.options.length; i++) {
		if (selObj.options[i].selected) {
			objCT.AddValue(selObj.options[i].value);
		}
	}
}

// Days selection changed
function SelectionChanged(objCT, arSel) {
	objCT.ResetValues();

	for(var i=0; i < arSel.length; i++)
		SelectionToValues(arSel[i], objCT);

	ShowResult();
}

// Days selection changed
function DaysChanged() {
	SelectionChanged(objDays,
		Array(document.getElementById("frmMain").Day0,
			document.getElementById("frmMain").Day1,document.getElementById("frmMain").Day2,
			document.getElementById("frmMain").Day3,document.getElementById("frmMain").Day4
		)
	);
}

// Months selection changed
function MonthsChanged() {
	SelectionChanged(objMonths, Array(document.getElementById("frmMain").Month0));
}

// Hours selection changed
function HoursChanged() {
	SelectionChanged(objHours, Array(document.getElementById("frmMain").Hour0,
		document.getElementById("frmMain").Hour1));
}

// Minutes selection changed
function MinutesChanged() {
	SelectionChanged(objMinutes,
		Array(document.getElementById("frmMain").Minute0,
			document.getElementById("frmMain").Minute1,
			document.getElementById("frmMain").Minute2,
			document.getElementById("frmMain").Minute3
		)
	);
}

// WeekDays selection changed
function WeekDaysChanged() {
	SelectionChanged(objWeekDays, Array(document.getElementById("frmMain").WeekDay0));
}

// Shows the result
function ShowResult() {
	document.getElementById("frmMain").Result.value =
		objMinutes.ValuesToString() + " " +
		objHours.ValuesToString() + " " +
		objDays.ValuesToString() + " " +
		objMonths.ValuesToString() + " " +
		objWeekDays.ValuesToString() + " ";
}

// Clears all selections of the Select object with MULTIPLE set
function ClearOne(selObj) {
	for(var i=0; i < selObj.options.length; i++) {
		if (selObj.options[i].selected)
			selObj.options[i].selected = false;
	}
}

// Clears all Days
function ClearDays() {

	ClearOne(document.getElementById("frmMain").Day0);
	ClearOne(document.getElementById("frmMain").Day1);
	ClearOne(document.getElementById("frmMain").Day2);
	ClearOne(document.getElementById("frmMain").Day3);
	ClearOne(document.getElementById("frmMain").Day4);

	DaysChanged();
}

// Clears all Months
function ClearMonths() {

	ClearOne(document.getElementById("frmMain").Month0);

	MonthsChanged();
}

// Clears all Hours
function ClearHours() {

	ClearOne(document.getElementById("frmMain").Hour0);
	ClearOne(document.getElementById("frmMain").Hour1);

	HoursChanged();
}

// Clears all Minutes
function ClearMinutes() {

	ClearOne(document.getElementById("frmMain").Minute0);
	ClearOne(document.getElementById("frmMain").Minute1);
	ClearOne(document.getElementById("frmMain").Minute2);
	ClearOne(document.getElementById("frmMain").Minute3);

	MinutesChanged();
}

// Clears all Week days
function ClearWeekDays() {

	ClearOne(document.getElementById("frmMain").WeekDay0);

	WeekDaysChanged();
}

// Clears all
function ClearAllCtls() {

	ClearDays();
	ClearMonths();
	ClearHours();
	ClearMinutes();
	ClearWeekDays();
}

// Array element operations class
//   element = col * R + row
//   row = element mod R
//   col = (element - element mod R) / R

function crtArrayElemntRow(element) {
	return element % this.RowMax;
}

function crtArrayElemntCol(element) {
	return (element - element % this.RowMax) / this.RowMax;
}

function crtArrayElements(ColMax,RowMax) {
	this.ColMax = ColMax;
	this.RowMax = RowMax;
	this.row = crtArrayElemntRow;
	this.col = crtArrayElemntCol;
}

// Sets one Ctrl to Now
function crtSetToNowOne(MaxCols,MaxRows,Element,CtrlName) {
	var s;
	var a = new crtArrayElements(MaxCols,MaxRows);

//alert(Element+":"+a.col(Element)+":"+a.row(Element));

	s = "document.getElementById('frmMain')."+CtrlName+a.col(Element)+
		".options[a.row(Element)].selected = true";

//alert(s);

	eval(s);

}


// Sets the controls to Now (Date/Time)
function crtSetToNow() {
	var d = new Date();
	//var d = new Date("March 1, 2003 13:23:00");

	ClearAllCtls();

	crtSetToNowOne(5,7,d.getDate()-1,"Day")
	DaysChanged();

	crtSetToNowOne(1,12,d.getMonth(),"Month")
	MonthsChanged();

	crtSetToNowOne(2,12,d.getHours(),"Hour")
	HoursChanged();

	crtSetToNowOne(4,15,d.getMinutes(),"Minute")
	MinutesChanged();

	crtSetToNowOne(1,7,d.getDay(),"WeekDay")
	WeekDaysChanged();
}
//*********** END OF User Interface *******************

//  End -->
