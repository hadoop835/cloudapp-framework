{
  "annotations": {
    "list": [
      {
        "builtIn": 1,
        "datasource": {
          "type": "grafana",
          "uid": "-- Grafana --"
        },
        "enable": true,
        "hide": true,
        "iconColor": "rgba(0, 211, 255, 1)",
        "name": "Annotations & Alerts",
        "type": "dashboard"
      }
    ]
  },
  "editable": true,
  "fiscalYearStartMonth": 0,
  "graphTooltip": 0,
  "id": 1,
  "links": [],
  "panels": [
    ${panel_templates}
  ],
  "preload": false,
  "schemaVersion": 40,
  "tags": [],
  "templating": {
    "list": [
      {
        "current": {
          "text": "prometheus"
        },
        "label": "Data Source",
        "name": "datasource",
        "options": [],
        "query": "prometheus",
        "refresh": 1,
        "regex": "",
        "type": "datasource"
      },
      {
        "current": {
          "text": "",
          "value": ""
        },
        "datasource": {
          "type": "prometheus",
          "uid": "<#noparse>${datasource}</#noparse>"
        },
        "definition": "label_values(target_info{},host_ip)",
        "description": "",
        "label": "Host IP",
        "multi": true,
        "name": "host_ip",
        "options": [],
        "query": {
          "qryType": 1,
          "query": "label_values(target_info{},host_ip)",
          "refId": "PrometheusVariableQueryEditor-VariableQuery"
        },
        "refresh": 1,
        "regex": "",
        "type": "query"
      }
    ]
  },
  "time": {
    "from": "now-15m",
    "to": "now"
  },
  "timepicker": {},
  "timezone": "browser",
  "title": "Cloud Application",
  "version": 1,
  "weekStart": ""
}
