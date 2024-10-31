import { useThemeColor } from "@/hooks/useThemeColor";
import React from "react";
import { StyleSheet, Text, View } from "react-native";
import { Tooltip } from "@rneui/themed";

type TooltipInfo = {
  display: boolean,
  field: string,
  message: string,
}

type TooltipProps = {
  field: string,
  tooltip: TooltipInfo,
  setTooltip: Function,
}

export function InvalidFieldTooltip({ field, tooltip, setTooltip }: TooltipProps) {
  const tooltipBackground = useThemeColor({}, 'tooltipBackground')

  return (
    <Tooltip
      visible={tooltip.display && (tooltip.field === field)}
      onClose={() => setTooltip({ display: false, field: '', message: '' })}
      containerStyle={styles.tooltipWrapper}
      backgroundColor={tooltipBackground}
      withPointer={true}
      popover={<Text>{tooltip.message}</Text>}
      overlayColor="transparent"
    />
  )
}

const styles = StyleSheet.create({
  tooltipWrapper: {
    padding: 5,
    width: 250,
  },
});
