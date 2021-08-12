package dialog;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.ui.components.JBScrollPane;
import model.Dimen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.Constants;
import util.ModelUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SingleDimenAllConfigDialog extends DialogWrapper {
    private JPanel controlPanel;
    private JBScrollPane scrollPane;
    private List<Component> bucketLabels = new ArrayList<>();
    private List<JCheckBox> selectionValues = new ArrayList<>();
    private List<JTextField> bucketScaleFactors = new ArrayList<>();
    private List<JButton> removeButtons = new ArrayList<>();
    private ArrayList<Dimen> data;
    GroupLayout layout;

    LabeledComponent<JBScrollPane> component;

    String suffixType;

    public SingleDimenAllConfigDialog(@Nullable Project project, String suffixType, ArrayList<Dimen> data) {
        super(project);
        this.suffixType = suffixType;
        this.data = data;
        setTitle(Constants.TITLE + suffixType + Constants.METRIC);
        initializePanel(suffixType);
        init();
    }

    private void clear() {
        for (int i = 0; i < bucketLabels.size(); i++) {
            if ( bucketLabels.get(i) != null) {
                bucketLabels.get(i).setVisible(false);
            }
            if (selectionValues.size() > i && selectionValues.get(i) != null) {
                selectionValues.get(i).setVisible(false);
            }
            if (bucketScaleFactors.size() > i && bucketScaleFactors.get(i) != null) {
                bucketScaleFactors.get(i).setVisible(false);
            }
            if (removeButtons.size() > i && removeButtons.get(i) != null) {
                removeButtons.get(i).setVisible(false);
            }
        }
        bucketLabels.clear();
        selectionValues.clear();
        bucketScaleFactors.clear();
        removeButtons.clear();
    }

    private void addInitialFields(String suffixType) {
        clear();
        for (int i = 0; i < data.size(); i++) {
            final Dimen dimen = data.get(i);
            JLabel bucketLabel = new JLabel();
            bucketLabels.add(bucketLabel);
            bucketLabel.setText(dimen.getBucket());
            final JTextField scaleFactor = new JTextField();
            bucketScaleFactors.add(scaleFactor);
            scaleFactor.setColumns(10);
            scaleFactor.getDocument().addDocumentListener(new DocumentListener() {
                private void setData() {
                    float val = 0;
                    try {
                        val = Float.parseFloat(scaleFactor.getText());
                    } catch (NullPointerException | NumberFormatException ex) {

                    }
                    if (suffixType.equals("dp")) {
                        dimen.setFactorDp(val);
                    } else if (suffixType.equals("px")) {
                        dimen.setFactorPx(val);
                    } else {
                        dimen.setFactorSp(val);
                    }
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    setData();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    setData();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    setData();
                }
            });
            bucketScaleFactors.get(i).setText((dimen.getFactor()) + "");

            JCheckBox selectedCheckBox = new JCheckBox();
            selectedCheckBox.setSelected(dimen.isSelected());
            selectedCheckBox.addChangeListener(e -> dimen.setSelected(selectedCheckBox.isSelected()));
            if (!dimen.isMandatory()) {
                JButton removeButton = new JButton("remove");
                removeButton.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeLayoutValues(dimen, bucketLabel, scaleFactor, selectedCheckBox, removeButton);
                        setLayoutConstraints();
                        layout.invalidateLayout(controlPanel);
                    }
                });
                removeButtons.add(removeButton);

            }
            selectionValues.add(selectedCheckBox);
        }

    }

    private void initializePanel(String suffixType) {
        controlPanel = new JPanel();
        scrollPane = new JBScrollPane(controlPanel, JBScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        addInitialFields(suffixType);
        layout = new GroupLayout(controlPanel);
        controlPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setLayoutConstraints();
        controlPanel.setPreferredSize(new Dimension(700,500));
//        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum()));
        component = LabeledComponent.create(scrollPane, "");
    }

    private void setLayoutConstraints() {
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        GroupLayout.ParallelGroup group1 = layout.createParallelGroup();
        GroupLayout.ParallelGroup group2 = layout.createParallelGroup();
        GroupLayout.ParallelGroup group3 = layout.createParallelGroup();
        GroupLayout.ParallelGroup group4 = layout.createParallelGroup();
        for (int i = 0; i < bucketLabels.size(); i++) {
            group1.addComponent(bucketLabels.get(i));
            group2.addComponent(bucketScaleFactors.get(i));
            group3.addComponent(selectionValues.get(i));
            if (i < removeButtons.size()) {

                group4.addComponent(removeButtons.get(i));
            }
        }
        hGroup.addGroup(group1);
        hGroup.addGroup(group2);
        hGroup.addGroup(group3);
        if (removeButtons.size() > 0) {
            hGroup.addGroup(group4);
        }
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        for (int i = 0, k = 0; i < bucketLabels.size(); i++) {
            GroupLayout.ParallelGroup parallelGroup = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            parallelGroup.addComponent(bucketLabels.get(i)).addComponent(bucketScaleFactors.get(i)).addComponent(selectionValues.get(i));
            if (!data.get(i).isMandatory()) {
                parallelGroup.addComponent(removeButtons.get(k++));
            }
            vGroup.addGroup(parallelGroup);
        }
        layout.setVerticalGroup(vGroup);

    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action[] actions = super.createActions();
        Action[] actionsAdd = new Action[actions.length + 5];
        for (int i = 0; i < actions.length; i++) {
            actionsAdd[i] = actions[i];
        }
        actionsAdd[actionsAdd.length - 5] = new DialogWrapperAction("保存配置一") {
            @Override
            protected void doAction(ActionEvent actionEvent) {
                String value = ModelUtil.toJson(data);
                PropertiesComponent.getInstance().setValue(Constants.SAVE_KEY_ALL_CONFIG_1, value);
                showTipsAlert("保存成功");
            }
        };
        actionsAdd[actionsAdd.length - 4] = new DialogWrapperAction("保存配置二") {
            @Override
            protected void doAction(ActionEvent actionEvent) {
                String value = ModelUtil.toJson(data);
                PropertiesComponent.getInstance().setValue(Constants.SAVE_KEY_ALL_CONFIG_2, value);
                showTipsAlert("保存成功");
            }
        };
        actionsAdd[actionsAdd.length - 3] = new DialogWrapperAction("配置一") {
            @Override
            protected void doAction(ActionEvent actionEvent) {
                String value = PropertiesComponent.getInstance().getValue(Constants.SAVE_KEY_ALL_CONFIG_1, Constants.INIT_MODEL_JSON);
                PropertiesComponent.getInstance().setValue(Constants.CURRENT_SAVE_KEY,Constants.SAVE_KEY_ALL_CONFIG_1);
                data = ModelUtil.fromJson(value);
                addInitialFields(suffixType);
                setLayoutConstraints();
                layout.invalidateLayout(controlPanel);
            }
        };
        actionsAdd[actionsAdd.length - 2] = new DialogWrapperAction("配置二") {
            @Override
            protected void doAction(ActionEvent actionEvent) {
                String value = PropertiesComponent.getInstance().getValue(Constants.SAVE_KEY_ALL_CONFIG_2, Constants.INIT_MODEL_JSON);
                PropertiesComponent.getInstance().setValue(Constants.CURRENT_SAVE_KEY,Constants.SAVE_KEY_ALL_CONFIG_2);
                data = ModelUtil.fromJson(value);
                addInitialFields(suffixType);
                setLayoutConstraints();
                layout.invalidateLayout(controlPanel);
            }
        };
        actionsAdd[actionsAdd.length - 1] = new DialogWrapperAction("Add Configuration") {
            @Override
            protected void doAction(ActionEvent actionEvent) {

                int invalidIndex = invalidBucketIndex();
                if (invalidIndex == -1) {
                    JTextField bucketValue = new JTextField();
                    bucketValue.setColumns(15);
                    bucketValue.setText(Constants.DEFAULT_BUCKET);
                    bucketValue.selectAll();

                    bucketLabels.add(bucketValue);
                    JTextField scaleFactor = new JTextField();
                    scaleFactor.setColumns(20);
                    scaleFactor.setText("3");
                    bucketScaleFactors.add(scaleFactor);
                    final JCheckBox selectedBucket = new JCheckBox();
                    selectedBucket.setSelected(true);
                    selectionValues.add(selectedBucket);
                    Dimen dimen = new Dimen()
                            .setBucket(Constants.DEFAULT_BUCKET)
                            .setFactorDp(Constants.DEFAULT_SCALE_FACTOR)
                            .setFactorSp(Constants.DEFAULT_SCALE_FACTOR)
                            .setFactorPx(Constants.DEFAULT_SCALE_FACTOR)
                            .setMandatory(false)
                            .setSelected(true);
                    data.add(dimen);
                    bucketValue.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            dimen.setBucket(bucketValue.getText());

                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            dimen.setBucket(bucketValue.getText());
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            dimen.setBucket(bucketValue.getText());

                        }
                    });
                    scaleFactor.getDocument().addDocumentListener(new DocumentListener() {
                        private void setData() {
                            float val = 0;
                            try {
                                val = Float.parseFloat(scaleFactor.getText());
                            } catch (NullPointerException | NumberFormatException ex) {

                            }

                            if (suffixType.equals("dp")) {
                                dimen.setFactorDp(val);
                            } else if (suffixType.equals("px")) {
                                dimen.setFactorPx(val);
                            } else {
                                dimen.setFactorSp(val);
                            }
                        }

                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            setData();

                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            setData();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            setData();
                        }
                    });
                    JButton removeButton = new JButton("remove");
                    removeButton.addActionListener(new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            removeLayoutValues(dimen, bucketValue, scaleFactor, selectedBucket, removeButton);
                            setLayoutConstraints();
                            layout.invalidateLayout(controlPanel);
                        }
                    });
                    removeButtons.add(removeButton);
                    setLayoutConstraints();
                    layout.invalidateLayout(controlPanel);
                } else {
                    showAlert(invalidIndex);
                }

            }

        };
        return actionsAdd;
    }

    private void removeLayoutValues(Dimen dimen, Component bucketValue, JTextField scaleFactor, JCheckBox selectedBucket, JButton removeButton) {
        data.remove(dimen);
        bucketValue.setVisible(false);
        bucketLabels.remove(bucketValue);
        scaleFactor.setVisible(false);
        bucketScaleFactors.remove(scaleFactor);
        selectedBucket.setVisible(false);
        selectionValues.remove(selectedBucket);
        removeButton.setVisible(false);
        removeButtons.remove(removeButton);
    }

    public int invalidBucketIndex() {
        HashMap<String, Boolean> containedBuckets = new HashMap<>();
        for (Dimen dimen : data) {
            if (containedBuckets.containsKey(dimen.getBucket())) {
                return Constants.ERROR_CODE[0];
            }
            containedBuckets.put(dimen.getBucket(), true);
        }

        return -1;
    }


    public void showAlert(int option) {
        JOptionPane optionPane = new JOptionPane(Constants.MESSAGES[option - 1], JOptionPane.WARNING_MESSAGE);
        JDialog dialog = optionPane.createDialog(Constants.ERROR_TITLE);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    public void showTipsAlert(String text) {
        JOptionPane optionPane = new JOptionPane(text, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(Constants.TIPS_TITLE);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return component;
    }

    public ArrayList<Dimen> getConversionValues() {
        return data;
    }
}
