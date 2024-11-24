import { useThemeColor } from "@/hooks/useThemeColor";
import React, { ReactNode } from "react";
import { StyleSheet, Text, View } from "react-native";
import { Tooltip, TooltipProps } from "@rneui/themed";

type TooltipInfo = {
  display: boolean,
  field: string,
  message: string,
}

type InvalidTooltipProps = {
  field: string,
  tooltip: TooltipInfo,
  setTooltip: Function,
  children: ReactNode,
}

export function InvalidFieldTooltip({ field, tooltip, setTooltip, children }: InvalidTooltipProps) {
  const tooltipBackground = useThemeColor({}, 'tooltipBackground')

  // Tooltip is used as a container in order for the caret placement and tooltip position to make sense among each other
  // (i.e. prevent the tooltip from displaying on top of the selected field)
  return (
    <Tooltip
      visible={tooltip.display && (tooltip.field === field)}
      onClose={() => setTooltip({ display: false, field: '', message: '' })}
      containerStyle={styles.tooltipWrapper}
      backgroundColor={tooltipBackground}
      withPointer={true}
      popover={<Text>{tooltip.message}</Text>}
      overlayColor="transparent"
    >
      {children}
    </Tooltip>
  )
}

const styles = StyleSheet.create({
  tooltipWrapper: {
    padding: 5,
    width: 250,
  },
});
