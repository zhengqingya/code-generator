// // eslint-disable-next-line no-unused-vars
// function qqLogin() {
//   var e = "1.0.1";
//   try {
//     e = opener.QC.getVersion()
//   } catch (e) {}
//   e = e ? "-" + e : e;
//   var t;
//   var a = /\/qzone\/openapi\/qc_loader\.js/i;
//   var r = document.getElementsByTagName("script");
//   for (var i = 0, c, n = r.length; i < n; i++) {
//     c = r[i];
//     var s = c.src || "";
//     var o = s.match(a);
//     if (o) {
//       t = c;
//       break
//     }
//   }
//   var p = "//qzonestyle.gtimg.cn/qzone/openapi/qc" + e + ".js";
//   var d = ["src=" + p + ""];
//   for (var i = 0, l; i < t.attributes.length; i++) {
//     l = t.attributes[i];
//     if (l.name != "src" && l.specified) {
//       d.push([l.name.toLowerCase(), '"' + l.value + '"'].join("="))
//     }
//   }
//   if (document.readyState != "complete") {
//     document.write("<script " + d.join(" ") + " ><" + "/script>")
//   } else {
//     var m = document.createElement("script"),
//       v;
//     m.type = "text/javascript";
//     m.src = p;
//     for (var i = d.length; i--;) {
//       v = d[i].split("=");
//       if (v[0] == "data-appid" || v[0] == "data-redirecturi" || v[0] == "data-callback") {
//         m.setAttribute(v[0], v[1].replace(/\"/g, ""))
//       }
//     }
//     var u = document.getElementsByTagName("head");
//     if (u && u[0]) {
//       u[0].appendChild(m)
//     }
//   }
// }
//
// export {
//   qqLogin
// }
