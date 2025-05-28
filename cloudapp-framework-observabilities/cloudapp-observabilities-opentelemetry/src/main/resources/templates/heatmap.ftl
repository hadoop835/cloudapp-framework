{
  "datasource": {
    "type": "prometheus",
    "uid": "<#noparse>${datasource}</#noparse>"
  },
  "fieldConfig": {
    "defaults": {
      "custom": {
        "hideFrom": {
          "legend": false,
          "tooltip": false,
          "viz": false
        },
        "scaleDistribution": {
          "type": "linear"
        }
      }
    },
    "overrides": []
  },
  "gridPos": {
    "h": 8,
    "w": 13,
    "x": 11,
    "y": ${metric_idx}
  },
  "id": ${idx},
  "options": {
    "calculate": false,
    "cellGap": 1,
    "color": {
      "exponent": 0.5,
      "fill": "blue",
      "mode": "opacity",
      "reverse": false,
      "scale": "linear",
      "scheme": "PRGn",
      "steps": 64
    },
    "exemplars": {
      "color": "blue"
    },
    "filterValues": {
      "le": 1e-9
    },
    "legend": {
      "show": true
    },
    "rowsFrame": {
      "layout": "auto"
    },
    "tooltip": {
      "mode": "single",
      "showColorScale": false,
      "yHistogram": false
    },
    "yAxis": {
      "axisPlacement": "left",
      "reverse": false,
      "unit": "none"
    }
  },
  "pluginVersion": "11.3.0-pre",
  "targets": [
    {
      "editorMode": "code",
      "exemplar": false,
      "expr": "sum by (le)(increase(${metric_name}_bucket{host_ip=~\"$host_ip\"}[1m]))",
      "format": "heatmap",
      "interval": "1m",
      "legendFormat": "{{le}}",
      "range": true,
      "refId": "A"
    }
  ],
  "title": "Heatmap",
  "type": "heatmap"
}
