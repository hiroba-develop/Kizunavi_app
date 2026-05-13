FROM node:20-alpine AS build

WORKDIR /app

COPY package.json package-lock.json .npmrc* ./
# package-lock.json に基づき厳密にインストールし、ローカルで検証済みのバージョンのみをデプロイする。
# 本番ビルドでは lockfile を必須とするため `npm ci` を使用する。
RUN npm ci

COPY . .
RUN npm run build

# ---

FROM node:20-alpine

WORKDIR /app

COPY --from=build /app/package.json /app/package-lock.json ./
COPY --from=build /app/node_modules ./node_modules
COPY --from=build /app/dist ./dist

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD node -e "fetch('http://localhost:5175/').then(r=>process.exit(r.ok?0:1)).catch(()=>process.exit(1))"

EXPOSE 5175

CMD ["npm", "run", "preview", "--", "--host", "0.0.0.0", "--port", "5175"]
