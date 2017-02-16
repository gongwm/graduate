/**
 * 
 */
var formulaSvg = (function() {
	var queue = MathJax.Hub.queue, math = null, tempDiv = '<div id="temp">${}$</div>';
	$("head").append(tempDiv);
	queue.Push(function() {
		math = MathJax.Hub.getAllJax("temp")[0];
	});
	var getSvg = function(formulaStr) {
		queue.Push([ "Text", math, "\\displaystyle{" + formulaStr + "}" ]);
		return $("#temp svg").prop("outerHTML");
	}
	return getSvg;
})();