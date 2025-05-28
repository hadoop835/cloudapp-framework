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
    "w": 11,
    "x": 0,
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
      "datasource": {
        "type": "prometheus",
        "uid": "<#noparse>${datasource}</#noparse>"
      },
      "editorMode": "code",
      "expr": "histogram_quantile(0.99, sum(rate(${metric_name}_bucket{host_ip=~\"$host_ip\"}[1m])) by (le, host_ip))",
      "legendFormat": "P99-{{host_ip}}",
      "range": true,
      "refId": "A"
    },
    {
      "datasource": {
        "type": "prometheus",
        "uid": "<#noparse>${datasource}</#noparse>"
      },
      "editorMode": "code",
      "expr": "histogram_quantile(0.95, sum(rate(${metric_name}_bucket{host_ip=~\"$host_ip\"}[1m])) by (le, host_ip))",
      "hide": false,
      "instant": false,
      "legendFormat": "P95-{{host_ip}}",
      "range": true,
      "refId": "B"
    },
    {
      "datasource": {
        "type": "prometheus",
        "uid": "<#noparse>${datasource}</#noparse>"
      },
      "editorMode": "code",
      "expr": "histogram_quantile(0.90, sum(rate(${metric_name}_bucket{host_ip=~\"$host_ip\"}[1m])) by (le, host_ip))",
      "hide": false,
      "instant": false,
      "legendFormat": "P90-{{host_ip}}",
      "range": true,
      "refId": "C"
    }
  ],
  "title": "P99/P95/P90",
  "type": "timeseries"
}
