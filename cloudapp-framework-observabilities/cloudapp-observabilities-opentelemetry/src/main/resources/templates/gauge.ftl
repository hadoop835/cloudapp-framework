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
      "datasource": {
        "type": "prometheus",
        "uid": "<#noparse>${datasource}</#noparse>"
      },
      "disableTextWrap": false,
      "editorMode": "code",
      "expr": "sum(irate(${metric_name}_total{host_ip=~\"$host_ip\"}[1m])) * 60",
      "fullMetaSearch": false,
      "includeNullMetadata": true,
      "legendFormat": "__auto",
      "range": true,
      "refId": "A",
      "useBackend": false
    }
  ],
  "title": "Increment(Per Minute)",
  "type": "gauge"
}