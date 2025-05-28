{
  "datasource": {
    "type": "prometheus",
    "uid": "<#noparse>${datasource}</#noparse>"
  },
  "fieldConfig": {
    "defaults": {
      "color": {
        "mode": "palette-classic"
      },
      "custom": {
        "axisBorderShow": false,
        "axisCenteredZero": false,
        "axisColorMode": "text",
        "axisLabel": "",
        "axisPlacement": "auto",
        "barAlignment": 0,
        "barWidthFactor": 0.6,
        "drawStyle": "line",
        "fillOpacity": 0,
        "gradientMode": "none",
        "hideFrom": {
          "legend": false,
          "tooltip": false,
          "viz": false
        },
        "insertNulls": false,
        "lineInterpolation": "linear",
        "lineWidth": 1,
        "pointSize": 5,
        "scaleDistribution": {
          "type": "linear"
        },
        "showPoints": "auto",
        "spanNulls": false,
        "stacking": {
          "group": "A",
          "mode": "none"
        },
        "thresholdsStyle": {
          "mode": "off"
        }
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
    "w": 18,
    "x": 6,
    "y": ${metric_idx}
  },
  "id": ${idx},
  "options": {
    "legend": {
    "calcs": [],
    "displayMode": "list",
    "placement": "bottom",
    "showLegend": true
    },
    "tooltip": {
      "mode": "single",
      "sort": "none"
    }
  },
  "pluginVersion": "11.3.0-pre",
  "targets": [
    {
      "editorMode": "code",
      "expr": "sum(rate(${metric_name}_total{host_ip=~\"$host_ip\"}[1m])) by (service_name)",
      "legendFormat": "{{service_name}}",
      "range": true,
      "refId": "A"
    }
  ],
  "title": "Change Rate(By Service)",
  "type": "timeseries"
}
