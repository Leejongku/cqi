<%@ page language="java" contentType="application/json; charset=utf-8" pageEncoding="utf-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%
  final class Json {
    String esc(String s) {
      if (s == null) return "";
      StringBuilder b = new StringBuilder(s.length() + 16);
      for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        switch (c) {
          case '\\': b.append("\\\\"); break;
          case '"':  b.append("\\\""); break;
          case '\b': b.append("\\b"); break;
          case '\f': b.append("\\f"); break;
          case '\n': b.append("\\n"); break;
          case '\r': b.append("\\r"); break;
          case '\t': b.append("\\t"); break;
          default:
            if (c < 0x20) {
              String hex = Integer.toHexString(c);
              b.append("\\u");
              for (int k = hex.length(); k < 4; k++) b.append('0');
              b.append(hex);
            } else { b.append(c); }
        }
      }
      return b.toString();
    }
    void writeVal(StringBuilder sb, Object v) {
      if (v == null) { sb.append("null"); return; }
      if (v instanceof String) { sb.append('"').append(esc((String)v)).append('"'); return; }
      if (v instanceof Number || v instanceof Boolean) { sb.append(String.valueOf(v)); return; }
      if (v instanceof java.util.Map) {
        sb.append('{');
        boolean first = true;
        for (Object ek : ((java.util.Map)v).keySet()) {
          if (!first) sb.append(',');
          first = false;
          String k = String.valueOf(ek);
          sb.append('"').append(esc(k)).append('"').append(':');
          writeVal(sb, ((java.util.Map)v).get(ek));
        }
        sb.append('}');
        return;
      }
      if (v instanceof java.util.Collection) {
        sb.append('[');
        boolean first = true;
        for (Object it : (java.util.Collection)v) {
          if (!first) sb.append(',');
          first = false;
          writeVal(sb, it);
        }
        sb.append(']');
        return;
      }
      sb.append('"').append(esc(String.valueOf(v))).append('"');
    }
  }
  Object mcqiMaster = request.getAttribute("mcqiMaster");
  StringBuilder sb = new StringBuilder(256);
  sb.append('{').append("\"mcqiMaster\":");
  new Json().writeVal(sb, mcqiMaster);
  sb.append('}');
  out.print(sb.toString());
%>
