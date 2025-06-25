package com.alibaba.cloudapp.starter.mail.refresh;

import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.starter.base.RefreshableComponent;
import com.alibaba.cloudapp.starter.mail.properties.CloudAppMailProperties;
import com.alibaba.cloudapp.starter.refresh.RefreshableProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StringUtils;

import javax.mail.Session;
import javax.naming.NamingException;
import java.util.Properties;

public class MailSenderRefreshableComponent extends RefreshableComponent<
        CloudAppMailProperties, JavaMailSenderImpl> implements BeanFactoryAware {
    
    private Session session;
    private BeanFactory beanFactory;
    
    public MailSenderRefreshableComponent(CloudAppMailProperties properties) {
        super(properties);
    }
    
    @Override
    public void postStart() {
        initProperties(bean);
    }
    
    @Override
    public void preStop() {
    
    }
    
    @Override
    public String bindKey() {
        return CloudAppMailProperties.PREFIX;
    }
    
    @Override
    public String getName() {
        return "cloudAppMail";
    }
    
    @Override
    protected JavaMailSenderImpl createBean(CloudAppMailProperties properties) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        if (StringUtils.hasText(properties.getJndiName())) {
            refreshSession(properties);
            sender.setDefaultEncoding(properties.getDefaultEncoding().name());
            sender.setSession(session);
        } else {
            sender.setHost(properties.getHost());
            if (properties.getPort() != null) {
                sender.setPort(properties.getPort());
            }
            initProperties(sender);
        }
        return sender;
    }
    
    private void initProperties(JavaMailSenderImpl sender) {
        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());
        sender.setProtocol(properties.getProtocol());
        
        if (properties.getDefaultEncoding() != null) {
            sender.setDefaultEncoding(properties.getDefaultEncoding().name());
        }
        
        if (!properties.getProperties().isEmpty()) {
            Properties prop = new Properties();
            prop.putAll(properties.getProperties());
            sender.setJavaMailProperties(prop);
        }
    }
    
    public void refreshSession(CloudAppMailProperties properties) {
        try {
            if (session != null) {
                RefreshableProxyFactory.updateProxyTarget(session, properties);
            } else {
                session = RefreshableProxyFactory.create(
                        this::createSession, properties
                );
                if(beanFactory instanceof ConfigurableBeanFactory) {
                    ConfigurableBeanFactory factory =
                            (ConfigurableBeanFactory) beanFactory;
                    factory.registerSingleton(
                            session.getClass().getName(), session);
                }
            }
        } catch(Exception e) {
            throw new CloudAppException("refresh session error!", e);
        }
    }
    
    private Session createSession(CloudAppMailProperties properties) {
        if (StringUtils.hasText(properties.getJndiName())) {
            try {
                return JndiLocatorDelegate
                        .createDefaultResourceRefLocator()
                        .lookup(properties.getJndiName(), Session.class);
                
            } catch (NamingException ex) {
                String msg = String.format(
                        "Unable to find Session in JNDI location %s",
                        properties.getJndiName()
                );
                throw new IllegalStateException(msg, ex);
            }
        } else {
            throw new CloudAppException("mail jndiName must not be empty!");
        }
    }
    
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
    
    public Session getSession() {
        return session;
    }
}
