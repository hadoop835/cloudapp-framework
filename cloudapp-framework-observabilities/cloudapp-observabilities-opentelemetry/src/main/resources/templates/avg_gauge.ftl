{
  "datasource": {
    "type": "prometheus",
    "uid": "<#noparse>${datasource}</#noparse>"
  },
  "fieldConfig": {
    "defaults": {
      "color": {
        "mode": "thresholds"
      },
      "mappings": [],
      "thresholds": {
        "mode": "absolute",
        "steps": [
          {
            "color": "green"
          },
          {
            "color": "red",
            "value": 80
          }
        ]
      }
    },
    "overrides": []
  },
  "gridPos": {
    "h": 8,
    "w": 6,
    "x": 0,
    "y": ${metric_idx}
  },
  "id": ${idx},
  "options": {
    "minVizHeight": 75,
    "minVizWidth": 75,
    "orientation": "auto",
    "reduceOptions": {
      "calcs": [
        "lastNotNull"
      ],
      "fields": "",
      "values": false
    },
    "showThresholdLabels": false,
    "showThresholdMarkers": true,
    "sizing": "auto"
  },
  "pluginVersion": "11.3.0-pre",
  "targets": [
    {
      "editorMode": "code",
      "expr": "avg(avg_over_time(${metric_name}{host_ip=~\"$host_ip\"}[1m]))",
      "legendFormat": "__auto",
      "range": true,
      "refId": "A"
    }
  ],
  "title": "Average Value",
  "type": "gauge"
}