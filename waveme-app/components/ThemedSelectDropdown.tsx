import React, { useState } from 'react';
import { View, Pressable, StyleSheet, Modal } from 'react-native';
import { ThemedText } from './theme/ThemedText';
import { ThemedView } from './theme/ThemedView';
import { useThemeColor } from '@/hooks/useThemeColor';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';

type DropdownItem = {
  title: string;
  value: string;
};

type ThemedSelectDropdownProps = {
  data: DropdownItem[];
  onSelect: (item: DropdownItem) => void;
  placeholder?: string;
  selectedValue?: string;
};

export default function ThemedSelectDropdown({ 
  data, 
  onSelect, 
  placeholder = 'Sélectionner...', 
  selectedValue 
}: ThemedSelectDropdownProps) {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState<DropdownItem | null>(
    selectedValue ? data.find(item => item.value === selectedValue) || null : null
  );

  const iconColor = useThemeColor({}, 'icon');
  const backgroundColor = useThemeColor({}, 'background');

  const handleSelect = (item: DropdownItem) => {
    setSelectedItem(item);
    setIsOpen(false);
    onSelect(item);
  };

  return (
    <View style={styles.container}>
      <Pressable 
        style={[styles.button, { borderColor: iconColor }]} 
        onPress={() => setIsOpen(!isOpen)}
      >
        <ThemedText style={styles.buttonText}>
          {selectedItem ? selectedItem.title : placeholder}
        </ThemedText>
        <Icon 
          name={isOpen ? 'chevron-up' : 'chevron-down'} 
          size={24} 
          color={iconColor} 
        />
      </Pressable>

      <Modal
        visible={isOpen}
        transparent
        animationType="fade"
        onRequestClose={() => setIsOpen(false)}
      >
        <Pressable 
          style={styles.overlay} 
          onPress={() => setIsOpen(false)}
        >
          <View style={[styles.dropdown, { backgroundColor }]}>
            {data.map((item, index) => (
              <Pressable
                key={index}
                style={[
                  styles.item,
                  index < data.length - 1 && { borderBottomWidth: 1, borderBottomColor: iconColor }
                ]}
                onPress={() => handleSelect(item)}
              >
                <ThemedText style={styles.itemText}>{item.title}</ThemedText>
              </Pressable>
            ))}
          </View>
        </Pressable>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginBottom: 12,
  },
  button: {
    height: 50,
    borderWidth: 1,
    borderRadius: 12,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 12,
  },
  buttonText: {
    flex: 1,
  },
  overlay: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  dropdown: {
    minWidth: 200,
    maxWidth: 300,
    borderRadius: 8,
    // Using only boxShadow for web compatibility (elevation removed)
    boxShadow: '0px 2px 3.84px rgba(0, 0, 0, 0.25)',
  },
  item: {
    paddingHorizontal: 16,
    paddingVertical: 12,
  },
  itemText: {
    textAlign: 'center',
  },
});
