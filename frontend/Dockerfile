# STAGE 1: Build-Umgebung (Vue/Vite Build)
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# -------------------------------------------------------------------
# STAGE 2: Laufzeit-Umgebung (Nginx)
# -------------------------------------------------------------------
FROM nginx:alpine AS final
RUN rm /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]